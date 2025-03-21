interface Activation {

    public double apply (double x);
    public double deriv (double x);
}

class Sigmoid implements Activation {

    @Override
    public double apply (double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double deriv (double x) {
        return x*(1-x);
    }
}
