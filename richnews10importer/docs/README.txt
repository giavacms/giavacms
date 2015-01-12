
inserire nel persistence.xml:
<jar-file>lib/richnews10importer-${project.version}.jar</jar-file>

creare una pagina di tipo RichContent


e lanciare lo script rs all'url:
http://localhost:8080/rest/v1/richnews10importer/import

una volta importato tutto, andare a dare la pagina corretta alle varie tipologie di news