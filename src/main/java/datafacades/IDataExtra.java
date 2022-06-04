package datafacades;

import java.util.List;

public interface IDataExtra<T> {
    List<T> getAllPaginated(int pageIndex, int numberOfRecords);
}
