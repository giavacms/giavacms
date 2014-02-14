rinominare le tabelle Product, Category, Product_Document,Product_Image in:

Category_Old
Product_Old
Product_Old_Document
Product_Old_Image 

inserire nel persistence.xml:
<jar-file>lib/category10importer-${project.version}.jar</jar-file>

creare una pagina di tipo Product

creare una pagina di tipo Category


e lanciare lo script rs all'url:
http://localhost:8080/rest/v1/catalogue10importer/import

una volta importato tutto, andare a dare la pagina corretta alle varie categorie