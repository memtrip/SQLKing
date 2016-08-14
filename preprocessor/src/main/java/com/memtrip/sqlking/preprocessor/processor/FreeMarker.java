package com.memtrip.sqlking.preprocessor.processor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

final class FreeMarker {
    private Configuration mConfiguration;

    private static final String TEMPLATE_ENCODING = "UTF-8";

    FreeMarker() throws IOException {
        mConfiguration = createConfiguration();
    }

    private Configuration createConfiguration() throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        configuration.setDefaultEncoding(TEMPLATE_ENCODING);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setClassForTemplateLoading(FreeMarker.class,"/");
        return configuration;
    }

    String getMappedFileBodyFromTemplate(String fileName, Map<String, Object> map) {
        try {
            return createFile(fileName, map);
        } catch (Exception e) {
            throw new IllegalStateException("Mapping template FAILED: " + e.getMessage());
        }
    }

    private String createFile(String file, Map<String, Object> map) throws TemplateException, IOException {
        Template template = getTemplate(file);

        Writer out = new StringWriter();
        template.process(map, out);
        String output = out.toString();
        out.close();

        return output;
    }

    private Template getTemplate(String fileName) throws IOException {
        return mConfiguration.getTemplate(fileName);
    }
}