## How to run

In order to make everything work, you need to create a postgres user. The username should be key-value-storage and the password - database-password. For this user you need to create a database called key-value-storage. In this database you have to create schemas trings_storage, numbers_storage and students_storage. Inside every scheme create a table with the same name as the scheme. You can see which columns to create in the corresponding Entity classes.