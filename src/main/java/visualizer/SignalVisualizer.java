package visualizer;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import java.awt.Font;

public class SignalVisualizer extends JFrame implements GLEventListener {

    private int[] signal;
    private GLCanvas canvas;

    public SignalVisualizer(int[] signal) {
        this.signal = signal;
        setTitle("Digital Signal Visualizer");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(this);
        add(canvas);
        setVisible(true);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0f, 0f, 0f, 1f); // Black background
        gl.glLineWidth(2f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        drawAxes(gl);
        drawSignal(gl);
        drawLevelsAndBits();
    }

    private void drawAxes(GL2 gl) {
        gl.glColor3f(1f, 1f, 1f); // White

        gl.glBegin(GL2.GL_LINES);
        // X-axis (time), from 0 (x=-1) to end (x=1) at y=0
        gl.glVertex2f(-1f, 0f);
        gl.glVertex2f(1f, 0f);
        // Y-axis line for levels at x=-1 (time=0)
        gl.glVertex2f(-1f, -1f);
        gl.glVertex2f(-1f, 1f);
        gl.glEnd();

        // Tick marks on Y axis at levels -1,0,1
        gl.glPointSize(5f);
        gl.glBegin(GL2.GL_POINTS);
        gl.glVertex2f(-1f, -1f);
        gl.glVertex2f(-1f, 0f);
        gl.glVertex2f(-1f, 1f);
        gl.glEnd();

        // Tick marks on X axis at signal points along time
        int n = signal.length;
        gl.glBegin(GL2.GL_POINTS);
        for (int i = 0; i < n; i++) {
            float x = mapSignalIndexToX(i, n);
            gl.glVertex2f(x, 0f);
        }
        gl.glEnd();
    }

    private void drawSignal(GL2 gl) {
        if (signal == null || signal.length == 0) return;

        gl.glColor3f(0f, 1f, 0f); // Green signal

        int n = signal.length;

        gl.glBegin(GL2.GL_LINE_STRIP);

        for (int i = 0; i < n; i++) {
            float x = mapSignalIndexToX(i, n);
            float y = mapSignalValueToY(signal[i]);

            if (i > 0) {
                float prevX = mapSignalIndexToX(i - 1, n);
                float prevY = mapSignalValueToY(signal[i - 1]);
                // horizontal line step
                gl.glVertex2f(prevX, prevY);
                gl.glVertex2f(x, prevY);
                // vertical line step
                gl.glVertex2f(x, prevY);
                gl.glVertex2f(x, y);
            } else {
                // vertical line at start
                gl.glVertex2f(x, 0f);
                gl.glVertex2f(x, y);
            }
        }
        float lastX = mapSignalIndexToX(n - 1, n);
        float lastY = mapSignalValueToY(signal[n - 1]);
        gl.glVertex2f(lastX, lastY);
        gl.glVertex2f(1f, lastY);
        gl.glEnd();
    }

    private void drawLevelsAndBits() {
        com.jogamp.opengl.util.awt.TextRenderer tr = new com.jogamp.opengl.util.awt.TextRenderer(
                new Font("SansSerif", Font.BOLD, 14));
        tr.beginRendering(canvas.getWidth(), canvas.getHeight());
        tr.setColor(1f, 1f, 1f, 1f);

        int height = canvas.getHeight();
        int width = canvas.getWidth();

        // Level labels on left beside y-axis
        int marginLeft = 30;

        // Y level positions corresponding to OpenGL y coordinates
        float yNeg1 = mapToScreenY(mapSignalValueToY(-1), height);
        float yZero = mapToScreenY(mapSignalValueToY(0), height);
        float yPos1 = mapToScreenY(mapSignalValueToY(1), height);

        tr.draw("-1", marginLeft, (int) yNeg1 - 7);
        tr.draw("0", marginLeft, (int) yZero - 7);
        tr.draw("1", marginLeft, (int) yPos1 - 7);

        // Draw bit values atop each signal pulse on chart
        int n = signal.length;
        int marginBottom = 60;

        for (int i = 0; i < n; i++) {
            float xNorm = mapSignalIndexToX(i, n);
            int screenX = (int) mapToScreenX(xNorm, width);
            float yLevel = mapSignalValueToY(signal[i]);
            int screenY = (int) mapToScreenY(yLevel, height);

            String bitChar = (signal[i] == 0) ? "0" : "1"; // Map signal to bit char, adjust as needed
            tr.draw(bitChar, screenX - 5, screenY - 25);
        }

        // Label X and Y axes
        tr.draw("Time (samples)", width / 2 - 60, 20);
        tr.draw("Amplitude Level", 5, height / 2 - 10);

        tr.endRendering();
    }

    // Helpers for coordinate mapping
    private float mapSignalIndexToX(int i, int length) {
        return -1f + 2f * i / (length - 1);
    }

    private float mapSignalValueToY(int val) {
        if (val > 1) val = 1;
        if (val < -1) val = -1;
        return val * 0.8f;
    }

    private float mapToScreenX(float normalizedX, int width) {
        return ((normalizedX + 1f) / 2f) * width;
    }

    private float mapToScreenY(float normalizedY, int height) {
        return ((1f - normalizedY) / 2f) * height;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();

        if (height <= 0) height = 1;

        float aspect = (float) width / height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (aspect >= 1) {
            gl.glOrtho(-1.0 * aspect, 1.0 * aspect, -1.0, 1.0, -1.0, 1.0);
        } else {
            gl.glOrtho(-1.0, 1.0, -1.0 / aspect, 1.0 / aspect, -1.0, 1.0);
        }

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public static void main(String[] args) {
        int[] sampleSignal = new int[]{1, 1, 0, -1, -1, 0, 1, 1, 0, -1};
        new SignalVisualizer(sampleSignal);
    }
}
