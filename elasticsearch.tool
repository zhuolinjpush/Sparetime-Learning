备注一些elasticsearch方法，平时常用
curl -XPOST http://localhost:19270/crash_log_info/type_v1/9d3c53716e61d95d419a960f32d7f4ef/_update -d '{
    "doc":{
        "message":"java.lang.StackOverflowError"
    }
}'
------------ ES ik ---------------
curl -XPUT 'http://localhost:19290/20170206/' -d '{
    "settings":{
        "number_of_shards":16,
        "number_of_replicas":1
    }
}'
curl -XPOST http://localhost:19290/20170206/type_v1/_mapping -d '
{
	"type_v1": {
         "_all": {
	        "analyzer": "ik_max_word",
	        "search_analyzer": "ik_max_word",
	        "term_vector": "no",
	        "store": "false"
        },
        "properties": {
        	"appkey": {
            	"ignore_above": 10240,
                "index": "not_analyzed",
                "type": "string"
            },
            "msg_id": {
                "type": "long"
            },
            "itime": {
                "type": "long"
            },
            "msg_content": {
                "type": "string",
                "analyzer": "ik_max_word",
                "search_analyzer": "ik_max_word",
                "include_in_all": "true",
                "boost": 8
            }
        }
    }
}'
