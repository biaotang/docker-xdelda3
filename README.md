# docker-xdelda3
This tool is used to generate docker images patch file and update docker images from patch file, base xdelta3  



First ,  install xdelta3、Jdk .

Example: 

​	old image                 tomcat:7

​	new image               tomcat:8

​	patch file path         /home/xdelda/test



to get patch file, execute the command as follow

```
java -jar docker-xdelta3-1.0.jar diff tomcat:7 tomcat:8 /home/xdelda/test
```

then, patch file  7-2-8.tar will generate under /home/xdelda/test/7-2-8/ 



to recover image,  execute the  command as follow

```
java -jar docker-xdelta3-1.0.jar recover tomcat:7 /home/xdelda/test/7-2-8.tar /home/xdelda/test
```

