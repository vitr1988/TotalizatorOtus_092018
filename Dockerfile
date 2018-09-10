FROM payara/server-full
COPY target/lottery.war $AUTODEPLOY_DIR/Lottery.war

#FROM airhacks/glassfish:v5
#COPY target/lottery.war $DEPLOYMENT_DIR/Lottery.war