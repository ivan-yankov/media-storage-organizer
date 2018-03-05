package org.yankov.mso.application.ui.controls;

import org.yankov.mso.application.Form;

public interface ProgressMonitor extends Form {

    void setWork(int work);

    void setStep(int step);

    void setOperation(String operation);

}
