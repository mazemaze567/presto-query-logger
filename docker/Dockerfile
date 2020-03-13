FROM presto:316

# Deploy Presto plugins and configs
COPY --chown=presto:presto tmp_work/target/* /usr/lib/presto/plugin/presto-query-logger/
COPY --chown=presto:presto tmp_work/plugin_config/* /usr/lib/presto/default/etc/

# Deploy test files
COPY --chown=presto:presto test/ /usr/lib/presto/test/

WORKDIR /usr/lib/presto
USER presto:presto
EXPOSE 8080
CMD ["/usr/lib/presto/bin/run-presto"]
