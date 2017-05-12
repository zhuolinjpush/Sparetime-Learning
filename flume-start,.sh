#! /bin/bash
bin=`dirname $0`
bin=`cd $bin;pwd`
confDir=$bin/conf
logDir=$bin/logs
agentName="push-target2"
mkdir $logDir >/dev/null 2>&1

export FLUME_HOME=/opt/push/flume
export PATH=$PATH:$FLUME_HOME/bin

nohup flume-ng agent -c $confDir -n $agentName -f $confDir/${agentName}.conf -Dflume.monitoring.agent=${agentName} -Dflume.monitoring.type=org.apache.flume.instrumentation.StatsDMonitorService -Dflume.monitoring.statsdServer=192.168.249.130 -Dflume.monitoring.statsdPort=8125 -Dflume.monitoring.pollFrequency=10 >>$logDir/run.log 2>&1 &
ps ux| grep flume | grep ${agentName} | grep -v grep|awk '{print $2}' >$bin/pid
