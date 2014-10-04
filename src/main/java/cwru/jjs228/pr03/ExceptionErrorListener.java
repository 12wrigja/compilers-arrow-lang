package cwru.jjs228.pr03;
import org.antlr.v4.runtime.*;

public class ExceptionErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e) {
        throw new RuntimeException(e.toString());
    }
}