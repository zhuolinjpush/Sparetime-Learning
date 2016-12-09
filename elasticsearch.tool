备注一些elasticsearch方法，平时常用
curl -XPOST http://localhost:19270/crash_log_info/type_v1/9d3c53716e61d95d419a960f32d7f4ef/_update -d '{
    "doc":{
        "message":"java.lang.StackOverflowError"
    }
}'
