1. clonare una pagina base esistente (es: news) per farne quella di base per il content type autori 

2. aggiungere richcontenttype 'author' e utilizzare la pagina appena clonata come base 

3. indicare 'author' come content type di tipo people 

4. inserire gli autori tramite people/edit.xhtml 

5. modificare la pagina post come segue

<f:event type="preRenderView" listener="#{peopleRequestController.setupByTitleAndType(richContentRequestController.element.author,'author')}" />

                <h4 class="title-divider">
                    <span>Autore</span>
                </h4>
                 <p style="text-align: justify; font-weight: bolder;"><small><h:outputText value="#{richContentRequestController.element.author}" /></small></p>
                <ui:fragment rendered="#{not empty peopleRequestController.element}">
                     <p style="text-align: justify;"><small><h:outputText value="#{peopleRequestController.element.content}" escape="false" /></small></p>
                </ui:fragment>
				<br />
