import sys
from rediscluster import StrictRedisCluster

cluster_nodes = [{"host":"172.16.101.150", "port":16387}]

rc = StrictRedisCluster(startup_nodes=cluster_nodes, decode_responses=True)

def query(key):
	res = rc.smembers(key)
	print res

if __name__=="__main__":
	query(sys.argv[1])
