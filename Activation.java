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

class Relu implements Activation {

    @Override
    public double apply (double x) {
        return Math.max(0,x);
    }

    @Override
    public double deriv (double x) {
        if (x < 0){
            return 0;
        }
        else {
            return 1;
        }
    }
}
