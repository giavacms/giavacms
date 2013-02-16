PrimeFaces.widget.Editor = function(B, A) {
	PrimeFaces.widget.Editor.superclass.constructor.call(this, B, A);
	if (A.resizable) {
		this.setupResize(A.widthHeightController)
	}
	if (A.language != undefined) {
		PrimeFaces.widget.EditorI18NProvider.applyLanguage(this, A.language)
	}
	if (A.title != undefined) {
		this.setTitle(A.title)
	}
};
YAHOO.lang.extend(PrimeFaces.widget.Editor, YAHOO.widget.Editor, {
	setupResize : function(A) {
		this.on("editorContentLoaded", function() {
			resize = new YAHOO.util.Resize(this.get("element_cont").get(
					"element"), {
				handles : [ "br" ],
				autoRatio : true,
				status : true,
				proxy : true,
				setSize : false
			});
			resize.on("startResize", function() {
				this.hide();
				this.set("disabled", true)
			}, this, true);
			resize.on("resize", function(C) {
				var D = C.height;
				var E = (this.toolbar.get("element").clientHeight + 2);
				var F = (this.dompath.clientHeight + 1);
				var B = (D - E - F);
				this.set("width", C.width + "px");
				this.set("height", B + "px");
				this.set("disabled", false);
				A.value = C.width + "," + B;
				this.show()
			}, this, true)
		})
	},
	setTitle : function(A) {
		this._defaultToolbar.titlebar = A
	}
});
PrimeFaces.widget.EditorI18NProvider = {
	messageBundle : {
		tr : {
			MSG_FONT_NAME_AND_SIZE : "Yaz\u0131 Tipi ve B\u00fcy\u00fckl\u00fc\u011f\u00fc",
			MSG_FONT_STYLE : "Yaz\u0131 Stili",
			MSG_UNDO_REDO : "Geri al/Yenile",
			MSG_ALIGNMENT : "Hizalama",
			MSG_PARAGRAPH_STYLE : "Paragraf Stili",
			MSG_INDENTING_AND_LISTS : "Girintiler ve Listeler",
			MSG_INSERT_ITEM : "Ekle",
			STR_IMAGE_BORDER : "\u0130maj kenarlar\u0131",
			STR_IMAGE_BORDER_SIZE : "\u0130maj kenar boyutu",
			STR_IMAGE_COPY : "\u0130maj kopyala",
			STR_IMAGE_BORDER_TYPE : "\u0130maj kenar tipi",
			STR_IMAGE_ORIG_SIZE : "\u0130maj ger\u00E7ek boyutu",
			STR_IMAGE_PADDING : "\u0130maj dolgusu",
			STR_IMAGE_PROP_TITLE : "\u0130maj ba\u015Fl\u0131\u011F\u0131",
			STR_IMAGE_SIZE : "\u0130maj b\u00FCy\u00FCkl\u00FC\u011F\u00FC",
			STR_IMAGE_TEXTFLOW : "\u0130maj yaz\u0131s\u0131",
			STR_IMAGE_TITLE : "\u0130maj \u00F6zellikleri",
			STR_IMAGE_URL : "\u0130maj adresini",
			STR_IMAGE_HERE : "\u0130maj adresi",
			STR_LINK_NEW_WINDOW : "Link yeni pencerede",
			STR_LINK_PROP_REMOVE : "Link \u00F6zellik sil ",
			STR_LINK_PROP_TITLE : "Link \u00F6zellikleri",
			STR_LINK_TITLE : "Link ba\u015Fl\u0131\u011F\u0131",
			STR_LOCAL_FILE_WARNING : "Link lokal dosya hatas\u0131",
			STR_NONE : "Bo\u015F"
		},
		pt : {
			MSG_FONT_NAME_AND_SIZE : "Nome e Tamanho da Fonte",
			MSG_FONT_STYLE : "Estilo da Fonte",
			MSG_UNDO_REDO : "Desfaz/Refaz",
			MSG_ALIGNMENT : "Alinhamento",
			MSG_PARAGRAPH_STYLE : "Estilo do Par\u00E1grafo",
			MSG_INDENTING_AND_LISTS : "Recuo/Listas",
			MSG_INSERT_ITEM : "Link / Img",
			STR_IMAGE_BORDER : "Borda",
			STR_IMAGE_BORDER_SIZE : "Tam",
			STR_IMAGE_COPY : "",
			STR_IMAGE_BORDER_TYPE : "Tipo",
			STR_IMAGE_ORIG_SIZE : "",
			STR_IMAGE_PADDING : "Margem",
			STR_IMAGE_PROP_TITLE : "Inserir Imagem",
			STR_IMAGE_SIZE : "Tam.",
			STR_IMAGE_TEXTFLOW : "Alinhar",
			STR_IMAGE_TITLE : "Descri\u00E7\00E3o",
			STR_IMAGE_URL : "Img. URL",
			STR_IMAGE_HERE : "",
			STR_LINK_NEW_WINDOW : "Abrir em uma nova janela",
			STR_LINK_PROP_REMOVE : "Remover Link",
			STR_LINK_PROP_TITLE : "Inserir Link",
			STR_LINK_TITLE : "Descri\u00E7\00E3o",
			STR_LOCAL_FILE_WARNING : "",
			STR_NONE : "none"
		}
	},
	applyLanguage : function(A, C) {
		if (this.messageBundle[C] != undefined) {
			var B = A._defaultToolbar.buttons;
			B[0].label = this.messageBundle[C].MSG_FONT_NAME_AND_SIZE;
			B[2].label = this.messageBundle[C].MSG_FONT_STYLE;
			B[6].label = this.messageBundle[C].MSG_UNDO_REDO;
			B[8].label = this.messageBundle[C].MSG_ALIGNMENT;
			B[10].label = this.messageBundle[C].MSG_PARAGRAPH_STYLE;
			B[12].label = this.messageBundle[C].MSG_INDENTING_AND_LISTS;
			B[14].label = this.messageBundle[C].MSG_INSERT_ITEM;
			A.STR_IMAGE_TITLE = this.messageBundle[C].STR_IMAGE_TITLE;
			A.STR_IMAGE_BORDER = this.messageBundle[C].STR_IMAGE_BORDER;
			A.STR_IMAGE_BORDER_SIZE = this.messageBundle[C].STR_IMAGE_BORDER_SIZE;
			A.STR_IMAGE_HERE = this.messageBundle[C].STR_IMAGE_HERE;
			A.STR_IMAGE_COPY = this.messageBundle[C].STR_IMAGE_COPY;
			A.STR_IMAGE_BORDER_TYPE = this.messageBundle[C].STR_IMAGE_BORDER_TYPE;
			A.STR_IMAGE_URL = this.messageBundle[C].STR_IMAGE_URL;
			A.STR_IMAGE_ORIG_SIZE = this.messageBundle[C].STR_IMAGE_ORIG_SIZE;
			A.STR_IMAGE_PADDING = this.messageBundle[C].STR_IMAGE_PADDING;
			A.STR_IMAGE_PROP_TITLE = this.messageBundle[C].STR_IMAGE_PROP_TITLE;
			A.STR_IMAGE_SIZE = this.messageBundle[C].STR_IMAGE_SIZE;
			A.STR_IMAGE_TEXTFLOW = this.messageBundle[C].STR_IMAGE_TEXTFLOW;
			A.STR_LINK_NEW_WINDOW = this.messageBundle[C].STR_LINK_NEW_WINDOW;
			A.STR_LINK_PROP_REMOVE = this.messageBundle[C].STR_LINK_PROP_REMOVE;
			A.STR_LINK_PROP_TITLE = this.messageBundle[C].STR_LINK_PROP_TITLE;
			A.STR_LINK_TITLE = this.messageBundle[C].STR_LINK_TITLE;
			A.STR_LOCAL_FILE_WARNING = this.messageBundle[C].STR_LOCAL_FILE_WARNING;
			A.STR_NONE = this.messageBundle[C].STR_NONE
		}
	}
};