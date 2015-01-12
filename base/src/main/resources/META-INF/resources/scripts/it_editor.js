PrimeFaces.widget.EditorI18NProvider.messageBundle["it"] = {
	MSG_FONT_NAME_AND_SIZE : "Nome e Tipo di Font",
	MSG_FONT_STYLE : "Stile",
	MSG_UNDO_REDO : "Avanti/Indietro",
	MSG_ALIGNMENT : "Allineamento",
	MSG_PARAGRAPH_STYLE : "Stile Paragrafo",
	MSG_INDENTING_AND_LISTS : "Rientro/Liste",
	MSG_INSERT_ITEM : "Link/Immagini",
	STR_IMAGE_BORDER : "Bordi",
	STR_IMAGE_BORDER_SIZE : "Tam",
	STR_IMAGE_COPY : "Copia",
	STR_IMAGE_BORDER_TYPE : "Tipo",
	STR_IMAGE_ORIG_SIZE : "Dimensione",
	STR_IMAGE_PADDING : "Margini",
	STR_IMAGE_PROP_TITLE : "Inserire Immagine",
	STR_IMAGE_SIZE : "Dimensioni",
	STR_IMAGE_TEXTFLOW : "Alleamento",
	STR_IMAGE_TITLE : "Titolo",
	STR_IMAGE_URL : "Img. URL",
	STR_IMAGE_HERE : "",
	STR_LINK_NEW_WINDOW : "Apri in nuova finestra",
	STR_LINK_PROP_REMOVE : "Rimuovi Link",
	STR_LINK_PROP_TITLE : "Inserisci Link",
	STR_LINK_TITLE : "Titolo",
	STR_LOCAL_FILE_WARNING : "Attenzione",
	STR_NONE : "nessuno"
};


function ridimensiona(fid,icId,w,h) {
	var ic = document.getElementById( fid + ':' +icId + ':input_container');
	var p = ic.children[1].children[2];
	p.style.width=w;
	p.style.height=h;
}

function addImageBrowser(theEditor,theUrl) {
	theEditor.on('toolbarLoaded', function() {
	    //When the toolbar is loaded, add a listener to the insertimage button
	    this.toolbar.on('insertimageClick', function() {
	        //Get the selected element
	        var _sel = this._getSelectedElement();
	        //If the selected element is an image, do the normal thing so they can manipulate the image
	        if (_sel && _sel.tagName && (_sel.tagName.toLowerCase() == 'img')) {
	            //Do the normal thing here..
	        } else {
	            //They don't have a selected image, open the image browser window
	            win = window.open(theUrl, 'IMAGE_BROWSER',
	                'left=20,top=20,width=500,height=500,toolbar=0,resizable=0,status=0');
	            if (!win) {
	                //Catch the popup blocker
	                alert('Please disable your popup blocker!!');
	            }
	            //This is important.. Return false here to not fire the rest of the listeners
	            return false;
	        }
	    }, this, true);
	}, theEditor, true);
}


