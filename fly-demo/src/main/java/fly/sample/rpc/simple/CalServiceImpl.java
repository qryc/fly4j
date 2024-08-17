package fly.sample.rpc.simple;

public class CalServiceImpl implements CalService {
    @Override
    public Integer sum(Integer a, Integer b) {
        return a + b;
    }
}
