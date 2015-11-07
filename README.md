SQLKing
======================

SQLKing is an Android SQLite ORM geared towards working with flat data structures. Tables are defined by Model classes and CRUD classes expose clean builders for executing queries.

####Gradle dependency####
```
dependencies {
    compile 'com.beyondconstraint.sqlking:SQLKing:1.0'
}
```

####Define your models###
SQL tables are defined by POJOs that implement the Model interface. The getter and setter methods must match the member variables, i.e; private String name; must be accompanied by getName() / setName(String newVal) methods.

```java
public class User implements Model {
    private String username;
    private long timestamp;
    private boolean isRegistered;
    private byte[] profilePicture;

    public String getUsername() {
        return username;
    }

    public void setUsername(String newVal) {
        username = newVal;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long newVal) {
        timestamp = newVal;
    }

    public boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean newVal) {
        isRegistered = newVal;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] newVal) {
        profilePicture = newVal;
    }
}
```

####Initialise the database####
SQLKing will create the database representation of your Model classes automatically, when these models are changed or new models are added, the version number argument must be incremented. NOTE: Incrementing the version number will drop and recreate the database. 

The SQLProvider should be attached to either the Application instance or a singleton.

```java
public class CustomApplication extends Application {
    private static CustomApplication sInstance;

    public static CustomApplication getInstance() {
        return sInstance;
    }

    public SQLProvider getSQLProvider() {
    	return mSQLProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

		Model[] model = new Model[] {
            	new User()
        };

        SQLInit SQLInit = new SQLInit(
	            DATABASE_NAME,
	            DATABASE_VERSION,
	            model,
	            mContext
        );

        mSQLProvider = new SQLProvider(new DefaultDatabaseEngine(SQLInit.getDatabase()));
    }
}
```

####Querying the database####
The `Insert`, `Select`, `Update`, `Delete` and `Count` classes are used to query the database, they use a `getBuilder()` method to add keywords and clauses. 


```java
User user = new User();
user.setUsername("12345678");
user.setIsRegistered(true);
user.setTimestamp(System.currentTimeMillis());

// INSERT INTO User (username, isRegistered, timestamp) VALUES ('12345678',true,632348968244);
long[] value = Insert.getBuilder().values(user).execute(CustomApplication.getSQLProvider());
```

```java
// SELECT * FROM User;
User[] users = Select.getBuilder().execute(User.class, CustomApplication.getSQLProvider());
```

```java
ContentValues contentValues = new ContentValues();
contentValues.put("isRegistered", true);
contentValues.put("timestamp", System.currentTimeMillis());

// UPDATE User SET isRegistered = 'true', timestamp = '123456789'
Update.getBuilder()
        .values(contentValues)
        .execute(User.class, getSQLProvider());
```

```java
// DELETE FROM User;
User[] users = Select.getBuilder().execute(User.class, CustomApplication.getSQLProvider());
```

```java
// SELECT Count(*) FROM User;
long count = Count.getBuilder().execute(User.class, CustomApplication.getSQLProvider());
```

####Clauses####
The `Where`, `And`, `In`, and `Or` classes are used to build up the query. `Where` is powered by the `Exp`ression enum:

```java
public enum Exp {
	EQUAL_TO ("="),
	MORE_THAN (">"),
	MORE_THAN_OR_EQUAL_TO (">="),
	LESS_THAN ("<"),
	LESS_THAN_OR_EQUAL_TO ("<="),
	LIKE ("LIKE");
}
``` 

The following illustrate how to build more complex queries: 

```java
// SELECT * FROM User WHERE isRegistered = 'true';
User[] users = Select.getBuilder()
        .where(new Where("isRegistered", Where.Exp.EQUAL_TO, true))
        .execute(User.class, CustomApplication.getSQLProvider());
```

```java
// SELECT * FROM User WHERE username LIKE 'jo%'
User[] users = Select.getBuilder()
        .where(new Where("username", Where.Exp.LIKE, "jo%"))
        .execute(User.class, CustomApplication.getSQLProvider());
```

```java
// SELECT * FROM User WHERE username IN ("sam","josh");
User[] users = Select.getBuilder()
        .where(new In("username", "sam", "josh"))
        .execute(User.class, CustomApplication.getSQLProvider());
```

```java
// SELECT * FROM User WHERE ((username = "sam" OR username = "angie") AND (timestamp >= 1234567890));
User[] users = Select.getBuilder()
		.where(new And(
                new Or(
                        new Where("username", Where.Exp.EQUAL_TO, "sam"),
                        new Where("username", Where.Exp.EQUAL_TO, "angie")
                ),
                new And(
                        new Where("timestamp", Where.Exp.MORE_THAN_OR_EQUAL_TO, 1234567890)
                )))
        .execute(User.class, CustomApplication.getSQLProvider());
```

####Keywords####
The `OrderBy` and `Limit` classes are used to manipulate the results of the `SELECT` class

```java
// SELECT * FROM user ORDER BY username DESC
User[] users = Select.getBuilder()
        .orderBy("username", OrderBy.Order.DESC)
        .execute(User.class, CustomApplication.getSQLProvider());
```

```java
// SELECT * FROM user ORDER BY username DESC LIMIT 2,4
User[] users = Select.getBuilder()
        .limit(2,4)
        .orderBy("username", OrderBy.Order.DESC)
        .execute(User.class, CustomApplication.getSQLProvider());
```

####Tests####
The `tests/java/com/beyondconstraint/sqlking` package contains a full set of unit and integration tests.

####TODO####
- Upgrade the schema gracefully, instead of dropping and recreating the database
- Use @annotation processing to auto generate query classes during the build process. This will attempt deprecate the reflection heavy areas of the library
- Add the Random() method to OrderBy
