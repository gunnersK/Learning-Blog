## 动态sql

- where标签

	- 先举个例子：

    ```
        <select id="findActiveBlogLike"
             resultType="Blog">
          SELECT * FROM BLOG 
          WHERE 
          <if test="state != null">
            state = #{state}
          </if> 
          <if test="title != null">
            AND title like #{title}
          </if>
          <if test="author != null and author.name != null">
            AND author_name like #{author.name}
          </if>
        </select>
    ```

	- 如果这些if条件没有一个能匹配上，最终这SQL会变成

    ```
        SELECT * FROM BLOG
        WHERE
    ```
    
	- 或者仅仅第二个条件匹配，就会变成

    ```
        SELECT * FROM BLOG
        WHERE
        AND title like 'someTitle'
    ```
		也不得
		
	- 所以可以用<where\>标签套在<if\>外面，where 元素知道只有在一个以上的if条件有值的情况下才去插入"WHERE"子句。而且，若最后的内容是"AND"或"OR"开头的，where 元素也会进行处理

    ```
        <select id="findActiveBlogLike"
            resultType="Blog">
            SELECT * FROM BLOG 
            <where> 
                <if test="state != null">
                state = #{state}
                </if> 
                <if test="title != null">
                AND title like #{title}
                </if>
                <if test="author != null and author.name != null">
                AND author_name like #{author.name}
                </if>
            </where>
        </select>
    ```
	
## javaType和jdbcType对应

- javaType="double" jdbcType="NUMERIC"

- javaType="string" jdbcType="VARCHAR"

- javaType="java.sql.Date" jdbcType="DATE"

- javaType="int" jdbcType="INTEGER"

- javaType="double" jdbcType="DOUBLE"

- javaType="long" jdbcType="INTEGER"

- javaType="string" jdbcType="CHAR"

- javaType="[B" jdbcType="BLOB"

- javaType="string" jdbcType="CLOB"

- javaType="float" jdbcType="FLOAT"

- javaType="java.sql.Timestamp" jdbcType="TIMESTAMP"