package omine.objects;

public class BlockReward {

    private final String name;
    private final String permission;
    private final String errorMessage;
    private final int delay;
    private final double experience;
    private final double money;
    private final double economy;
    private final boolean item1;
    private final boolean item2;
    private final boolean item3;

    public BlockReward(String name, String permission, String errorMessage, int delay, double experience, double money, double economy, boolean item1, boolean item2, boolean item3) {
        this.name = name;
        this.permission = permission;
        this.errorMessage = errorMessage;
        this.delay = delay;
        this.experience = experience;
        this.money = money;
        this.economy = economy;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public int getDelay() {
        return delay;
    }

    public double getExperience() {
        return experience;
    }

    public double getMoney() {
        return money;
    }

    public double getEconomy() {
        return economy;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean getItem1() {
        return item1;
    }

    public boolean getItem2() {
        return item2;
    }

    public boolean getItem3() {
        return item3;
    }
}
