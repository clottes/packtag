/**
 * Project pack:tag >> http://packtag.sf.net
 *
 * This software is published under the terms of the LGPL
 * License version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * 
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2008/03/15 16:37:07 $
 * Revision:      $Revision: 1.7 $
 * 
 * $Log: ScriptTag.java,v $
 * Revision 1.7  2008/03/15 16:37:07  danielgalan
 * cleaning
 *
 * Revision 1.6  2008/01/26 12:53:28  danielgalan
 * virtual path for combined resources (per resourcetype)
 *
 * Revision 1.5  2007/11/21 20:02:26  danielgalan
 * Don't force a charset, when pack:tag is not enabled
 *
 * Revision 1.4  2007/10/31 09:23:11  danielgalan
 * extends now PackTag
 *
 * Revision 1.3  2007/10/13 20:23:34  danielgalan
 * added charset support, MIME-Type abstraction
 *
 * Revision 1.2  2007/10/02 21:21:04  danielgalan
 * Make JavaScript-Tag XHTML conform
 *
 * Revision 1.1  2007/04/22 19:04:24  danielgalan
 * pack.tag moved from subversion to good old CVS
 *
 */
package net.sf.packtag.tag;

import javax.servlet.jsp.JspWriter;

import net.sf.packtag.implementation.JsminPackStrategy;
import net.sf.packtag.strategy.PackStrategy;



/**
 * JSP Tag for compressing JavaScript resources.
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.7 $
 */
public class ScriptTag extends PackTag {

	private static final long serialVersionUID = -5324074433734258409L;

	private final static String STRATEGY_NAME_JAVASCRIPT = "script";

	private final static String SCRIPT_START = "<script type=\"text/javascript\" src=\"";
	private final static String SCRIPT_END_SOLO = "</script>";
	private final static String SCRIPT_SRC_END = "\"";

	/** Name of the async attribute */
	private final static String ATTRIBUTE_ASYNC = "async";
	/** Name of the defer attribute */
	private final static String ATTRIBUTE_DEFER = "defer";

        /**
	 * The charset is needed for IE to process utf-8 (IE isn't a webbrowser .. it's a disease)
	 * All Browser (Firefox, Opera, .. evaluate the Content-Type header for each included resource (servlet).
	 * But for the IE, the charset has to be specified in the link-reference :-/
	 * Well, at least it's usefull for the cache.type "file" ;)
	 */
	private final static String OUTPUT_CHARSET = " charset=\"utf-8\"";
	private final static String OUTPUT_ASYNC_XHTML = " async=\"async\"";
	private final static String OUTPUT_DEFER_XHTML = " defer=\"defer\"";
	private final static String OUTPUT_ASYNC_HTML5 = " async";
	private final static String OUTPUT_DEFER_HTML5 = " defer";
	private final static String SCRIPT_END = ">" + SCRIPT_END_SOLO;

	private final static String MIME_TYPE_JAVASCRIPT = "text/javascript";
	private final static String EXTENSION_JAVASCRIPT = "js";

	/** If async is set to true, the async attribute is added */
	private Boolean async = Boolean.FALSE;
	/** If defer is set to true, the defer attribute is added */
	private Boolean defer = Boolean.FALSE;

/* LABJS start */
	private final static String SCRIPT_START_SOLO = "<script type=\"text/javascript\"";

	@Override
	protected void writeResouce(final JspWriter writer, final String path) throws Exception {
		StringBuffer buffer = new StringBuffer();
		if(getLabjs() != null){
			if(isTag()){
				buffer.append(SCRIPT_START_SOLO);
				if (isEnabled()) {
					buffer.append(OUTPUT_CHARSET);
				}
				if (Boolean.TRUE.equals(isAsync())) {
			                if (isScriptAsyncdeferXhtml()) {
					buffer.append(OUTPUT_ASYNC_XHTML);
			                }
			                else {
					buffer.append(OUTPUT_ASYNC_HTML5);
			                }
				}
				if (Boolean.TRUE.equals(isDefer())) {
			                if (isScriptAsyncdeferXhtml()) {
					buffer.append(OUTPUT_DEFER_XHTML);
			                }
			                else {
					buffer.append(OUTPUT_DEFER_HTML5);
			                }
				}
				buffer.append(">");
			}

			//now to laod via labjs and .script:
			buffer.append(getLabjs())
			.append(".script(\"")
			.append(path)
			.append("\");");

			if(isTag()) {
				buffer.append(SCRIPT_END_SOLO);
			}
		}else{
			buffer.append(SCRIPT_START);
			buffer.append(path);
			buffer.append(SCRIPT_SRC_END);
			if (isEnabled()) {
				buffer.append(OUTPUT_CHARSET);
			}
			if (Boolean.TRUE.equals(isAsync())) {
				if (isScriptAsyncdeferXhtml()) {
					buffer.append(OUTPUT_ASYNC_XHTML);
				}
				else {
					buffer.append(OUTPUT_ASYNC_HTML5);
				}
			}
			if (Boolean.TRUE.equals(isDefer())) {
				if (isScriptAsyncdeferXhtml()) {
					buffer.append(OUTPUT_DEFER_XHTML);
				}
				else {
					buffer.append(OUTPUT_DEFER_HTML5);
				}
			}
			buffer.append(SCRIPT_END);
		}
/* LABJS end */
		writer.write(buffer.toString());
	}


	/** Should the resource be async? */
	public Boolean isAsync() {
		return async;
	}


	/** Sets if the resource should be async */
	public void setAsync(final String async) {
		if (isStandardTaglibAvailable()) {
			this.async = new Boolean((String)evaluate(ATTRIBUTE_ASYNC, async, String.class));
		}
		else {
			this.async = new Boolean(async);
		}
	}


	/** Should the resource be async? */
	public Boolean isDefer() {
		return defer;
	}


	/** Sets if the resource should be async */
	public void setDefer(final String defer) {
		if (isStandardTaglibAvailable()) {
			this.defer = new Boolean((String)evaluate(ATTRIBUTE_DEFER, defer, String.class));
		}
		else {
			this.defer = new Boolean(defer);
		}
	}


	@Override
	protected String getStrategyName() {
		return STRATEGY_NAME_JAVASCRIPT;
	}


	@Override
	protected PackStrategy getResourceDefaultStrategy() {
		return new JsminPackStrategy();
	}


	@Override
	protected String getResourceExtension() {
		return EXTENSION_JAVASCRIPT;
	}


	@Override
	protected String getMimeType() {
		return MIME_TYPE_JAVASCRIPT;
	}

}
