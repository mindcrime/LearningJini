#!/bin/bash

java -Djava.security.manager -Djava.security.policy=server.policy -Djava.rmi.server.useCodebaseOnly=false -jar lib/start.jar start-myproj-client.config
