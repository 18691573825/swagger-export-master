1、修改index.html文件中的
                    <div>
                  		运营服务平台  ： <a href="/getWord?tag=work" target="_blank">Doc</a>&nbsp;&nbsp;&nbsp;
                  		<a href="http://localhost:8000/swagger-ui.html" target="_blank">Swagger</a><br/>
                  	</div>
中的tag值 使其和 application.yml中的
                    module:
                      list:
                        - tag: work
                          swaggerUrl: http://localhost:8000/v2/api-docs
                          exportDocName: 云服务平台
中的tag值 保持一致

2、修改 上面标签中的href和下面的swaggerUrl的出json的地址为自己服务的

3、启动本服务和自己的服务，访问localhost:1314 就可以导出了