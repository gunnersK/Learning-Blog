-  <a href="#Preconditions">Preconditions</a>

- ## <a name="Preconditions">Preconditions</a>

- checkNotNull(T reference, @Nullable String errorMessageTemplate,@Nullable Object... errorMessageArgs)

    - 检验对象是否为空，为空时抛异常，异常信息为指定的错误信息，且允许在错误信息中使用占位符。	

		```
			String str = null;
        	Preconditions.checkNotNull(str, "%s-%s is null", "String", "str");
        	
        	java.lang.NullPointerException: String-str is null
		```
	
	