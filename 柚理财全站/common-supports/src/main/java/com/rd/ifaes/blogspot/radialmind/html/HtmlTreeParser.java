// $ANTLR 3.0.1 /workspace/xssprotect/trunk/grammar/htmlTreeParser.g 2008-01-06 10:06:05

	package com.rd.ifaes.blogspot.radialmind.html;
	
	import java.io.IOException;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.blogspot.radialmind.html.HTMLParser;

public class HtmlTreeParser extends TreeParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlTreeParser.class);
	
    private static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "TAG_START_OPEN", "TAG_END_OPEN", "TAG_CLOSE", "TAG_EMPTY_CLOSE", "WS", "QUOTECHAR", "WSCHAR", "ATTR_VALUE", "PCDATA", "LETTER", "NAMECHAR", "GENERIC_ID", "DIGIT", "SPECIALCHAR", "Tokens", "ELEMENT", "ATTRIBUTE", "SETTING"
    };
    public static final int TAG_CLOSE=6;
    public static final int LETTER=13;
    public static final int ATTRIBUTE=20;
    public static final int TAG_END_OPEN=5;
    public static final int WSCHAR=10;
    public static final int EOF=-1;
    public static final int Tokens=18;
    public static final int NAMECHAR=14;
    public static final int PCDATA=12;
    public static final int TAG_EMPTY_CLOSE=7;
    public static final int WS=8;
    public static final int SETTING=21;
    public static final int SPECIALCHAR=17;
    public static final int GENERIC_ID=15;
    public static final int ELEMENT=19;
    public static final int ATTR_VALUE=11;
    public static final int DIGIT=16;
    public static final int QUOTECHAR=9;
    public static final int TAG_START_OPEN=4;

        public HtmlTreeParser(TreeNodeStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "/workspace/xssprotect/trunk/grammar/htmlTreeParser.g"; }


    /**
     * 
     * @throws RecognitionException
     */
    public final void document() throws RecognitionException {
        try {
            {
            pushFollow(FOLLOW_element_in_document43);
            element();
            _fsp--;
            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end document

    /**
     * 
     * @throws RecognitionException
     */
    public final void element() throws RecognitionException {
        Tree name=null;
        Tree attrName=null;
        Tree value=null;
        Tree text=null;

        try {
            {
            match(input,ELEMENT,FOLLOW_ELEMENT_in_element58); 

            match(input, Token.DOWN, null); 
            name=(Tree)input.LT(1);
            match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_element62); 
             HTMLParser.openTag( name.getText() ); 
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ATTRIBUTE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    {
            	    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_element111); 

            	    match(input, Token.DOWN, null); 
            	    attrName=(Tree)input.LT(1);
            	    match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_element115); 
            	    value=(Tree)input.LT(1);
            	    match(input,ATTR_VALUE,FOLLOW_ATTR_VALUE_in_element119); 

            	    match(input, Token.UP, null); 
            	     HTMLParser.addAttribute( attrName.getText(), value.getText() ); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==SETTING) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    {
            	    match(input,SETTING,FOLLOW_SETTING_in_element186); 

            	    match(input, Token.DOWN, null); 
            	    attrName=(Tree)input.LT(1);
            	    match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_element190); 

            	    match(input, Token.UP, null); 
            	     HTMLParser.addAttribute( attrName.getText(), "" ); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

             HTMLParser.finishAttributes(); 
            loop3:
            do {
                int alt3=3;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ELEMENT) ) {
                    alt3=1;
                }
                else if ( (LA3_0==PCDATA) ) {
                    alt3=2;
                }


                switch (alt3) {
            	case 1 :
            	    {
            	    pushFollow(FOLLOW_element_in_element254);
            	    element();
            	    _fsp--;


            	    }
            	    break;
            	case 2 :
            	    {
            	    text=(Tree)input.LT(1);
            	    match(input,PCDATA,FOLLOW_PCDATA_in_element272); 
            	     HTMLParser.addText( text.getText() ); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

             HTMLParser.closeTag( name.getText() ); 

            match(input, Token.UP, null); 

            }

        }
        catch (IOException e) {

        	LOGGER.error(e.getMessage(), e);
            	
        }
        return ;
    }
    // $ANTLR end element


 

    public static final BitSet FOLLOW_element_in_document43 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ELEMENT_in_element58 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_GENERIC_ID_in_element62 = new BitSet(new long[]{0x0000000000381008L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_element111 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_GENERIC_ID_in_element115 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_ATTR_VALUE_in_element119 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SETTING_in_element186 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_GENERIC_ID_in_element190 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_element_in_element254 = new BitSet(new long[]{0x0000000000081008L});
    public static final BitSet FOLLOW_PCDATA_in_element272 = new BitSet(new long[]{0x0000000000081008L});

}