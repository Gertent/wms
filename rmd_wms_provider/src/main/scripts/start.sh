#!/bin/bash  
cd `dirname $0`  
BIN_DIR=`pwd`  
cd ..  
DEPLOY_DIR=`pwd`  
CONF_DIR=$DEPLOY_DIR/conf  
#从conf/dubbo.properties 中获取配置信息 
SERVER_NAME=`sed '/dubbo.application.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`  
SERVER_PORT=`sed '/dubbo.protocol.port/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`  
LOGS_FILE=`sed '/dubbo.log4j.file/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`  
JMX_PORT=`sed '/dubbo.jmx.port/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`  
  
if [ -z "$SERVER_NAME" ]; then  
    SERVER_NAME=`hostname`  
fi  
#判断服务是否启动
PIDS=`ps  --no-heading -C java -f --width 1000 | grep "$CONF_DIR" |awk '{print $2}'`  
if [ -n "$PIDS" ]; then  
    echo "ERROR: The $SERVER_NAME already started!"  
    echo "PID: $PIDS"  
    exit 1  
fi  
  
if [ -n "$SERVER_PORT" ]; then  
    SERVER_PORT_COUNT=`netstat -tln | grep $SERVER_PORT | wc -l`  
    if [ $SERVER_PORT_COUNT -gt 0 ]; then  
        echo "ERROR: The $SERVER_NAME port $SERVER_PORT already used!"  
        exit 1  
    fi  
fi  
  
LOGS_DIR=""  
if [ -n "$LOGS_FILE" ]; then  
    LOGS_DIR=`dirname $LOGS_FILE`  
else  
    LOGS_DIR=$DEPLOY_DIR/logs  
fi  
if [ ! -d $LOGS_DIR ]; then  
    mkdir $LOGS_DIR  
fi  
  
LIB_DIR=$DEPLOY_DIR/lib  
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`  
  
JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "  
JAVA_DEBUG_OPTS=""  
#命令行获取参数，是否以debug方式启动
if [ "$1" = "debug" ]; then  
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "  
fi 
#设置jmx端口变量（从dubbo配置文件中获取）
JAVA_JMX_OPTS=""
if [ -n "$JMX_PORT" ]; then
	JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
fi
#JVM启动参数
JAVA_MEM_OPTS=" -server -Xmx1g -Xms1g -Xmn256m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "  
  
#启动服务，不进行日志输出
echo -e "Starting the $SERVER_NAME ...\c"  
nohup java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -classpath $CONF_DIR:$LIB_JARS com.alibaba.dubbo.container.Main > /dev/null 2>&1 &  


#校验服务是否正常启动
COUNT=0  
while [ $COUNT -lt 1 ]; do      
    echo -e ".\c"  
    sleep 1   
    if [ -n "$SERVER_PORT" ]; then  
        COUNT=`echo status | nc 127.0.0.1 $SERVER_PORT -i 1 | grep -c OK`  
    else  
        COUNT=`ps  --no-heading -C java -f --width 1000 | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`  
    fi  
    if [ $COUNT -gt 0 ]; then  
        break  
    fi  
done  
echo "OK!"  
PIDS=`ps  --no-heading -C java -f --width 1000 | grep "$DEPLOY_DIR" | awk '{print $2}'`  
echo "PID: $PIDS"  
