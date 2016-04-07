package Models.Portfolio;

import Models.Market.MarketEquity;
import Models.Transaction.Transaction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Contains user equityholdings and cash accounts.
 */
public class Portfolio extends Observable implements Observer,Serializable  {
    private ArrayList<HoldingEquity> holdingEquities;
    private ArrayList<CashAccount> cashAccounts;
    private ArrayList<Transaction> transactionLog;
    private String hashedPass;
    private String id;
    private HashMap<String, Boolean> cashAccountNames;

    //Used to denote serialized version
    private static final long serialVersionUID = 681129221878275270L;

    /**
     * Constructor
     * @param hashedPass the hashed password to be associated with this object
     */
    public Portfolio(String hashedPass, String id){
        //create default array lists
        holdingEquities =  new ArrayList<>();
        cashAccounts = new ArrayList<>();
        transactionLog = new ArrayList<>();

        //save the hashed password
        this.hashedPass = hashedPass;
        this.id = id;

        //instantiate the cash account name map
        cashAccountNames = new HashMap<>();

    }

    public ArrayList<CashAccount> getCashAccounts() {
        return cashAccounts;
    }

    public boolean getCashAccNameExists(String name){return cashAccountNames.containsKey(name);}

    public ArrayList<Transaction> getTransactionLog() {
        return transactionLog;
    }

    public ArrayList<HoldingEquity> getHoldingEquities() {
        return holdingEquities;
    }

    /**
     * returns the password
     * @return the password
     */
    public String getPassword(){ return hashedPass; }
    public String getId(){ return id; }

    public void addTransaction(Transaction transaction){
        transactionLog.add(transaction);

        setChanged();
        notifyObservers();
    }


    /**
     * creates and adds a specific number of shares of the given equity to this portfolio
     * @param target the equity that is being purchased
     * @param numShares the number of shares that are being aquired
     */
    public void buyEquity(MarketEquity target, int numShares){
        //add target equity to HoldingEquity with numShares
        HoldingEquity newEquity = new HoldingEquity(numShares,target.getValue(),target.getName(), target.getTickerSymbol(), target);
        holdingEquities.add(newEquity);

        setChanged();
        notifyObservers(ObserveType.HOLDING_EQUITY);
    }


    public void sellEquity(HoldingEquity target, int numShares){
        //remove target number of shares from Equity holding, **if shares goes to 0 delete object?
        target.setNumShares((target.getNumShares()-numShares));

        if(target.getNumShares() <= 0) {
            int index = holdingEquities.indexOf(target);
            holdingEquities.remove(index);
        }

        setChanged();
        notifyObservers(ObserveType.HOLDING_EQUITY);
    }

    /**
     * creates and adds a cash account object to this portfolio with the given name and initial balance
     * @param name the name requested for the cash account
     * @param initialBalance the starting balance of the account
     */
    public void addCashAccount(String name, float initialBalance){
        //add target cash account with initial balance
        CashAccount newCashAccount = new CashAccount(name, initialBalance);
        cashAccounts.add(newCashAccount);

        //update the names hashmap
        cashAccountNames.put(name, Boolean.TRUE);

        setChanged();
        notifyObservers(ObserveType.CASH_ACCOUNT);
    }

    /**
     * deletes a cash account from the portfolio
     * @param target the cash account to  be deleted
     */
    public void removeCashAccount(CashAccount target){
        //update teh names hashmap
        cashAccountNames.remove(target.getName());

        //remove target cash account
        int index = cashAccounts.indexOf(target);
        cashAccounts.remove(index);


        setChanged();
        notifyObservers(ObserveType.CASH_ACCOUNT);
    }

    /**
     * returns the total value of equities in the portfolio.
     * @return value the total value fo the portfolio
     */
    public float getPortfolioValue() {
        float value = 0;

        for (int i = 0; i < holdingEquities.size(); i++) {
            value += holdingEquities.get(i).getValue();
        }

        return value;
    }

    @Override
    public void update(Observable o, Object arg){


    }

}
