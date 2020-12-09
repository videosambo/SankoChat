package fi.videosambo.sankochat.module;

import org.pf4j.PluginWrapper;

public abstract class Module implements Comparable<Module>{

    protected ModuleManager moduleManager;
    protected int priority;
    protected boolean violationPolicy = true;
    protected boolean needsHistory = false;

    public void setModuleManager(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public boolean isViolationPolicy() {
        return violationPolicy;
    }

    public void setViolationPolicy(boolean violationPolicy) {
        this.violationPolicy = violationPolicy;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Module m) {
        return Integer.compare(this.getPriority(), m.getPriority());
    }

    public boolean needsHistory() {
        return needsHistory;
    }

    public void setNeedsHistory(boolean needsHistory) {
        this.needsHistory = needsHistory;
    }
}
