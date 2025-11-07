package fipaan.com.console;

import fipaan.com.console.style.*;
import fipaan.com.has.*;
import java.io.OutputStream;

public interface IConsoleBuffer<Self extends IConsoleBuffer<Self>> extends HasRect<Self> {
    // --- Audio / Alerts ---
    Self ringBellAction();

    // --- Screen Modes ---
    Self setScreenModeAction(ConsoleScreenMode mode);
    Self resetScreenModeAction(ConsoleScreenMode mode);
    Self onLinuxResetXAction();

    // --- Screen Management ---
    Self oldClearScreenAction();
    Self eraseScreenAction();
    Self eraseStoredLinesAction();
    Self saveScreenAction();
    Self restoreScreenAction();
    Self scrollUpAction();
    Self moveToNextPageAction();

    // --- Erase Operations ---
    Self eraseCursorToEOSAction();
    Self eraseCursorToBOSAction();
    Self eraseCursorToEOLAction();
    Self eraseCursorToBOLAction();
    Self eraseLineAction();
    Self removeAtCursorAction();

    // --- Cursor Control ---
    Self setCursorVisibleAction(boolean isCursorVisible);
    Self setCursorShapeAction(CursorShape shape);
    Self requestPositionAction();
    Self deleteLastAtCurrentLineAction();

    // --- State Save / Restore ---
    Self saveDECAction();
    Self restoreDECAction();
    Self saveSCOAction();
    Self restoreSCOAction();

    // --- Drawing / Styles ---
    Self setLineDrawingModeAction(boolean isDrawing);
    Self setStyleAction(ConsoleStyle style);
    Self applyStylesAction(ConsoleStyleAny[] styles);
    Self setColor16Action(ConsoleColor16 color);
    Self setColor256Action(ConsoleColor256 color);

    // --- Input / Output ---
    Self writeCodepointAction(int key);
    Self writeOutAction(OutputStream out, String text);
}
