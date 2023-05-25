package konovalov;

import net.lingala.zip4j.ZipFile;

import javax.swing.*;
import java.io.File;

import static konovalov.GUIForm.password;

public class DecryptorThread extends Thread {

    private final GUIForm form;
    private File file;

    public DecryptorThread(GUIForm form) {
        this.form = form;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        onStart();
        try {
            String outPath = getOutputPath();
            ZipFile zipFile = new ZipFile(file, password);
            zipFile.extractAll(outPath);
            onFinish();
        } catch (Exception ex) {
            if (ex.getMessage().contains("Wrong Password")) {
                showWrongPasswordWarning();
            } else if (ex.getMessage().contains("empty or null password provided")) {
                showEmptyPasswordWarning();
            } else {
                ex.printStackTrace();
            }
        }
    }

    private void onStart() {
        form.setButtonEnabled(false);
    }

    private void onFinish() {
        form.setButtonEnabled(true);
        form.showFinished();
    }

    private String getOutputPath() {
        String path = file.getAbsolutePath().replaceAll("\\.enc$", "");
        for (int i = 1; ; i++) {
            String number = i > 1 ? Integer.toString(i) : "";
            String outPath = path.substring(0, path.lastIndexOf('.')) + number
                    + path.substring(path.lastIndexOf('.'));
            if (!new File(outPath).exists()) {
                return outPath;
            }
        }

    }

    public void showWrongPasswordWarning() {
        JOptionPane.showMessageDialog(form.getRootPanel(), "Пароль указан неверно!",
                "Ошибка", JOptionPane.WARNING_MESSAGE);
    }

    public void showEmptyPasswordWarning() {
        JOptionPane.showMessageDialog(form.getRootPanel(), "Пароль не указан!",
                "Ошибка", JOptionPane.WARNING_MESSAGE);
    }
}
