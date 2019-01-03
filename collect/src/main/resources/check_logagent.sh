#!/bin/sh

function whether_changed(){
    local file_path=${1}
    local check_time=${2}
    #agentID=`ps -ef | grep check_logagent.sh | grep -v 'grep' | awk '{print $2}'`
    #kill -9 $agentID
    while [[ true ]]; do
        file_old_change="`stat ${file_path}|grep Change`"
        sleep ${check_time}
        file_new_change="`stat ${file_path}|grep Change`"

        if [[ `echo ${file_old_change}` == `echo ${file_new_change}` ]]; then
            echo "### In ${check_time}s ,${file_path} does not change ###" >> /opt/data_collect/log_agent/logs/check.log
            /opt/data_collect/log_agent/log_agent.sh stop
            sleep 20
            /opt/data_collect/log_agent/log_agent.sh start
        fi

    done
}

whether_changed /opt/data_collect/log_agent/logs/info.log 4000
