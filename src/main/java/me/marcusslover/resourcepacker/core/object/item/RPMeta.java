package me.marcusslover.resourcepacker.core.object.item;

public class RPMeta {
    private int customModelData;

    public RPMeta() {
        this(-1);
    }

    public RPMeta(int customModelData) {
        this.customModelData = customModelData;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public int customModelData() {
        return customModelData;
    }
}
