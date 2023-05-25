package konovalov;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import java.io.File;

import static konovalov.GUIForm.password;

public class EncryptorThread extends Thread{
    private final GUIForm form;
    private File file;
    private final ZipParameters parameters = new ZipParameters();

    public EncryptorThread(GUIForm form){
        this.form = form;
    }
    public void setFile(File file){
        this.file = file;
    }
    @Override
    public void run() {
        onStart();
        encrypt();
        onFinish();
    }
    private void onStart(){
        form.setButtonEnabled(false);
    }
    private void encrypt() {
        String archiveName = getArchiveName();
        ZipFile zipFile = new ZipFile(archiveName, password);
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        parameters.setCompressionLevel(CompressionLevel.ULTRA);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(EncryptionMethod.AES);
        parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
        try {
            if (file.isDirectory()) {
                zipFile.addFolder(file, parameters);
            } else {
                zipFile.addFile(file, parameters);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void onFinish(){
        form.setButtonEnabled(true);
        form.showFinished();
    }
    private String getArchiveName() {
        for (int i = 1; ; i++) {
            String number = i > 1 ? Integer.toString(i) : "";
            String archiveName;
            if (file.getAbsolutePath().matches("[a-zA-Z0-9_\\\\:]+\\.[a-z]+")) {
                String[] parts = file.getAbsolutePath().split("\\.");
                archiveName = parts[0] + number + ".enc";
            } else {
                archiveName = file.getAbsolutePath() + number + ".enc";
            }
            if (!new File(archiveName).exists()) {
                return archiveName;
            }
        }
    }
}
