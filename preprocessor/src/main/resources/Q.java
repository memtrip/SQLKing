package com.memtrip.sqlking.gen;

import android.database.Cursor;
import android.content.ContentValues;
import com.memtrip.sqlking.common.SQLQuery;
import com.memtrip.sqlking.common.Resolver;

import java.util.List;
import java.util.ArrayList;

public class Q {

    public static class DefaultResolver implements Resolver {

        @Override
        public SQLQuery getSQLQuery(Class<?> classDef) {
            <#assign isAssignableFrom>
                <#list tables as table>
                } else if (classDef.isAssignableFrom(${table.getPackage()}.${table.getName()}.class)) {
                    return new ${table.getName()}();
                </#list>
                }
            </#assign>

            ${isAssignableFrom?trim?remove_beginning("} else ")} else {
                throw new IllegalStateException("Please ensure all SQL tables are annotated with @Table");
            }
        }
    }

    <#list tables as table>

        <#assign getColumnNames>
            <#list table.getPrimitiveColumns(tables) as column>
                "${table.getName()}.${column.getName()}",
            </#list>
        </#assign>

        <#assign unionInsertColumnNames><#list table.getPrimitiveColumns(tables) as column>${column.getName()},</#list></#assign>

        <#assign packagedTableName>
            ${table.getPackage()}.${table.getName()}
        </#assign>

        public static class ${table.getName()} implements SQLQuery {

            <#list table.getColumns() as column>
                public static final String ${formatConstant(column.getName())} = "${column.getName()}";
            </#list>

            @Override
            public String getTableName() {
                return "${table.getName()}";
            }

            @Override
            public String getTableInsertQuery() {
                return ${assembleCreateTable(table, tables)}
            }

            @Override
            public String[] getIndexNames() {
                return new String[]{
                <#list table.getPrimitiveColumns(tables) as column>
                    <#if column.isIndex()>
                        "${table.getName()}_${column.getName()}_index",
                    </#if>
                </#list>
                };
            }

            @Override
            public String getCreateIndexQuery() {
                StringBuilder sb = new StringBuilder();

                <#list table.getPrimitiveColumns(tables) as column>
                    <#if column.isIndex()>
                        sb.append("CREATE INDEX ${table.getName()}_${column.getName()}_index ON ${table.getName()} (${column.getName()});");
                    </#if>
                </#list>

                return (sb.length() > 0) ? sb.toString() : null;
            }

            @Override
            public String[] buildUnionInsertQuery(Object[] modelsToInsert) {
                List<List<Object>> chunks = new ArrayList<>();
                List<Object> models = new ArrayList<>();

                for (int i = 0; i < modelsToInsert.length; i++) {
                    models.add(modelsToInsert[i]);

                    if ((i+1) % 500 == 0) {
                        chunks.add(new ArrayList<>(models));
                        models.clear();
                    }
                }

                if (models.size() > 0) {
                    chunks.add(models);
                }

                String[] unionInsertQueries = new String[chunks.size()];
                for (int i = 0; i < chunks.size(); i++) {
                    unionInsertQueries[i] = buildInsertQuery(chunks.get(i));
                }

                return unionInsertQueries;
            }

            private String buildInsertQuery(List<Object> models) {
                StringBuilder sb = new StringBuilder();

                sb.append("INSERT INTO ${table.getName()} ");

                for (int i = 0; i < models.size(); i++) {
                    ${packagedTableName} ${table.getName()?lower_case} = (${packagedTableName})models.get(i);

                    if (i == 0){
                        sb.append("(${unionInsertColumnNames?remove_ending(",")}) ");
                        sb.append("SELECT ");

                        <#list table.getPrimitiveColumns(tables) as column>
                            <#assign getter>
                                ${table.getName()?lower_case}.get${column.getName()?cap_first}()
                            </#assign>

                            sb.append("${getInsertValue(column.getType(),getter)} AS ${column.getName()}, ");
                        </#list>

                        sb.delete(sb.length()-2,sb.length());
                    } else {
                        sb.append(" UNION ALL SELECT ");

                        <#list table.getPrimitiveColumns(tables) as column>
                            <#assign getter>
                                ${table.getName()?lower_case}.get${column.getName()?cap_first}()
                            </#assign>
                            sb.append("${getInsertValue(column.getType(),getter)}, ");
                        </#list>

                        sb.delete(sb.length()-2,sb.length());
                    }
                }

                return sb.toString();
            }

            private String assembleBlob(byte[] val) {
                if (val != null) {
                    StringBuilder sb = new StringBuilder();

                    for (byte b : val)
                        sb.append(String.format("%02X ", b));

                    return sb.toString();
                } else {
                    return "NULL";
                }
            }

            @Override
            public ${packagedTableName}[] retrieveSQLSelectResults(Cursor cursor) {
                ${packagedTableName}[] result = new ${packagedTableName}[cursor.getCount()];

                cursor.moveToFirst();
                for (int i = 0; !cursor.isAfterLast(); i++) {
                    ${packagedTableName} ${table.getName()?lower_case} = new ${packagedTableName}();

                    ${joinReferences(table.getName(),tables)}

                    for (int x = 0; x < cursor.getColumnCount(); x++) {
                        <#assign retrieveSQLSelectResults>
                            <#list table.getColumns() as column>
                                <#if column.isJoinable(tables)>
                                    ${join(column.getClassName(),tables)}
                                <#else>
                                    } else if (cursor.getColumnName(x).equals(${formatConstant(column.getName())})) {
                                        ${table.getName()?lower_case}.set${column.getName()?cap_first}(${getCursorGetter(column.getType())});
                                </#if>
                            </#list>
                            }
                        </#assign>

                        ${retrieveSQLSelectResults?trim?remove_beginning("} else ")}
                    }

                    result[i] = ${table.getName()?lower_case};
                    cursor.moveToNext();
                }

                cursor.close();

                return result;
            }

            @Override
            public String[] getColumnNames() {
                return new String[]{${getColumnNames?remove_ending(",")}};
            }

            @Override
            public ContentValues getContentValues(Object model) {
                ${packagedTableName} ${table.getName()?lower_case} = (${packagedTableName})model;

                ContentValues contentValues = new ContentValues();

                <#list table.getPrimitiveColumns(tables) as column>
                    contentValues.put(${formatConstant(column.getName())}, ${table.getName()?lower_case}.get${column.getName()?cap_first}());
                </#list>

                return contentValues;
            }
        }

    </#list>
}