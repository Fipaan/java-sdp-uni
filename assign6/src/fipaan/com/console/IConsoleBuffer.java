package fipaan.com.console;

import fipaan.com.console.style.*;
import fipaan.com.has.HasRect;
import java.io.OutputStream;

// TODO: merge with ConsoleBuffer and implement default handleKey(s),
//       maybe convert to abstract class
public interface IConsoleBuffer<Self extends IConsoleBuffer<Self>> extends HasRect<Self> {
    /* buffer / output */
    Self writeInBufferAction(int code);
    Self writeInBufferAction(int[] codes);
    Self writeOutAction(OutputStream out, String str);

    /* simple controls */
    Self resetLineAction();
    Self newLineAction();
    Self horTabAction();
    Self verTabAction();
    Self formFeedAction();
    Self backspaceAction();
    Self deleteAction();
    Self bellAction();

    /* screen / DEC/SCO */
    Self clearScreenAction();
    Self moveUpAction();
    Self saveDECAction();
    Self restoreDECAction();
    Self saveSCOAction();
    Self restoreSCOAction();
    Self storeScreenAction(boolean store);
    Self requestPositionAction();

    /* erase */
    Self eraseCursorToEOSAction();
    Self eraseCursorToBOSAction();
    Self eraseScreenAction();
    Self eraseStoredLinesAction();
    Self eraseCursorToEOLAction();
    Self eraseCursorToBOLAction();
    Self eraseLineAction();

    /* styles / colors / cursor shape */
    Self setLineDrawingModeAction(boolean enable);
    Self setStyleAction(ConsoleStyle style);
    Self setColor16Action(ConsoleColor16 color);
    Self applyStyleSeqAction(ConsoleStyleAny style);
    Self setCursorShapeAction(CursorShape shape);

    /* screen/private modes */
    Self screenModeAction(ConsoleScreenMode mode, boolean enable);
    Self cursorVisibilityAction(boolean visible);
}
