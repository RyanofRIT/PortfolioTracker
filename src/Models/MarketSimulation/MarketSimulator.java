package Models.MarketSimulation;

import java.util.ArrayList;

/**
 * Manages all the simulations for the market.
 */
public class MarketSimulator {
    private ArrayList<PortfolioMemento> mementos;
    private float basePortfolioValue;

    public MarketSimulator(float basePortfolioValue) {
        this.mementos = new ArrayList<PortfolioMemento>();
        this.basePortfolioValue = basePortfolioValue;
    }

    /**
     * Creates a simulation then creates a memento.
     * @return  memento the memento
     */
    public PortfolioMemento createMemento(float percent, int steps, StepType stepType, SimulationType type){

        Simulation simulation;
        switch (type) {
            case BEAR:
                simulation = new BearSimulation(percent, steps, this.basePortfolioValue, stepType);
                break;

            case BULL:
                simulation = new BullSimulation(percent, steps, this.basePortfolioValue, stepType);
                break;

            case NO_GROWTH:
                simulation = new NoGrowthSimulation(percent, steps, this.basePortfolioValue, stepType);
                break;

            default:
                simulation = new NoGrowthSimulation(percent, steps, this.basePortfolioValue, stepType);
                break;
        }

        return new PortfolioMemento(simulation);
    }

    /**
     * Pops the last memento off the stack and sets the
     * basePortfolioValue to its value. Used to undo a simulation.
     */
    public float popMemento(){
        int lastIndex = mementos.size() - 1;
        PortfolioMemento memory = this.mementos.remove(lastIndex);
        this.basePortfolioValue = memory.getSimulation().getEquitiesValue();

        return basePortfolioValue;
    }

    /**
     * Runs the simulation. It first creates a PortfolioMemento then calls the
     * memento's evaluate method and adds it to mementos.
     * @param percent   the percentage to run the simulation at. In decimal form
     * @param steps     the number of steps to take while running the simulation
     * @param stepType  the type of steps to make. Day, Month, or Year
     * @param type      the type of symulation. Bear, Bull, or No Growth
     * @return  valuesAtSteps   the array of values mapped to each step
     */
    public ArrayList<Float> runSimulation(float percent, int steps, StepType stepType, SimulationType type){
        percent /= 100;
        PortfolioMemento memento = createMemento(percent, steps, stepType, type);
        ArrayList<Float> valuesAtSteps = memento.getSimulation().evaluate();

        mementos.add(memento);

        //update basePortfolioValue to the evaluated value so
        //we can run another simulation from this point.
        int lastIndex = valuesAtSteps.size() - 1;
        this.basePortfolioValue = valuesAtSteps.get(lastIndex);

        return valuesAtSteps;
    }

}
