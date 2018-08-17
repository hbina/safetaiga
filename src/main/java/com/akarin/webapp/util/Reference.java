package com.akarin.webapp.util;

public class Reference {

    public static class Web {

        public final static String ROOT = "/";
        public final static String TEXTBOARD_ROOT = "/textboard";
        public final static String TEXTBOARD_BOARD = "/textboard/:boardlink";
        public final static String TEXTBOARD_BOARD_THREAD = "/textboard/:boardlink/:threadid";
        public final static String IMAGEPROCESSING_ROOT = "/com/akarin/webapp/imageprocessing";
        public final static String MANIFESTO_ROOT = "/manifesto";
    }

    public static class Templates {

        /**
         * My templates
         */
        public final static String ROOT = "/velocity/root.vm";
        public final static String TEXTBOARD = "/velocity/textboard/textboard.vm";
        public final static String TEXTBOARD_BOARD = "/velocity/textboard/board.vm";
        public final static String TEXTBOARD_BOARD_THREAD = "/velocity/textboard/thread.vm";
        public final static String IMAGE_PROCESSING_UPLOAD = "/velocity/imageprocessing/imageupload.vm";
        public final static String DISPLAY_IMAGE = "/velocity/imageprocessing/displayimage.vm";
        public final static String IMAGE_PROCESSING = "/velocity/imageprocessing/imageprocessing.vm";
        public final static String IMAGE_UPLOAD = "/velocity/imageprocessing/imageupload.vm";
        public final static String MANIFESTO_MAINPAGE = "/velocity/manifesto/manifesto.vm";

        /**
         * ERROR templates
         */
        public final static String NOT_FOUND = "/velocity/notFound.vm";
        public final static String ERROR = "/velocity/error.vm";
    }

    public static class Names {
        public final static String ROOT = "ROOT";
        public final static String TEXTBOARD = "TEXTBOARD";
        public final static String IMAGEPROCESSING = "IMAGE PROCESSING";
        public final static String MANIFESTO = "MANIFESTO";
    }

    private static class ErrorHandlers {

        /**
         * ERROR HANDLER VOCABULARIES <a href="$RETURN_LINK">$RETURN_NAME</a>
         */
        public final static String RETURNLINK = "RETURN_LINK";
        public final static String RETURNNAME = "RETURN_NAME";
        public final static String ERROR = "ERROR";
    }

    static class Textboard {

        static class Database {
            /**
             * DATABASE TEXTBOARD VOCABULARIES
             */

            public final static String BOARDLINK = "boardlink";
            public final static String BOARDNAME = "boardname";
            public final static String THREADID = "threadid";
            public final static String THREADTEXT = "threadtext";
            public final static String POSTID = "postid";
            public final static String POSTTEXT = "posttext";
            public final static String BOARDDESCRIPTION = "boarddescription";
        }
    }

    public static class CommonStrings {
        /**
         * ERROR HANDLER VOCABULARIES <a href="$RETURN_LINK">$RETURN_NAME</a>
         */
        public final static String RETURNLINK = "RETURN_LINK";
        public final static String RETURNNAME = "RETURN_NAME";
        public final static String ERROR = "ERROR";

        /**
         * PATHS CONSTANTS
         */
        public final static String LINK_ROOT = "/";
        public final static String LINK_TEXTBOARD = "/textboard/";
        public final static String LINK_IMAGEPROCESSING = "com/akarin/webapp/imageprocessing";
    }

    public static class VelocityVariables {
        public final static String WEBPAGE_DEFAULT_ICON = "WEBPAGE_DEFAULT_ICON";
        public final static String ROOT_LINK = "ROOT_LINK";
        public final static String ROOT_NAME = "ROOT_NAME";
        public final static String TEXTBOARD_LINK = "TEXTBOARD_LINK";
        public final static String TEXTBOARD_NAME = "TEXTBOARD_NAME";
        public final static String IMAGEPROCESSING_LINK = "IMAGEPROCESSING_LINK";
        public final static String IMAGEPROCESSING_NAME = "IMAGEPROCESSING_NAME";
        public final static String MANIFESTO_LINK = "MANIFESTO_LINK";
        public final static String MANIFESTO_NAME = "MANIFESTO_NAME";
        public final static String WHERE_NAME = "WHERE_NAME";
        public final static String WHERE_TEXT = "WHERE_TEXT";
        public final static String BOARDLIST = "boardList";
        public final static String THREADLIST = "threadList";
        public final static String POSTLIST = "postList";
        public final static String INPUT_BOARDLINK = "INPUT_BOARDLINK";
        public final static String INPUT_BOARDNAME = "INPUT_BOARDNAME";
        public final static String INPUT_BOARDDESCRIPTION = "INPUT_BOARDDESCRIPTION";
        public final static String INPUT_THREAD = "INPUT_THREAD";
        public final static String INPUT_THREADTEXT = "INPUT_THREADTEXT";
        public final static String INPUT_POSTTEXT = "INPUT_POSTTEXT";

    }
}
