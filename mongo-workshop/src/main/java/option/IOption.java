package option;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IOption<T> {
	
	public <U> U visit(Supplier<U> onNone, Function<T, U> onSome);

}
