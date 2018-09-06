FROM payara/server-full
COPY target/lottery.war $AUTODEPLOY_DIR/Lottery.war