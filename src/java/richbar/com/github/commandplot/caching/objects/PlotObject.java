package richbar.com.github.commandplot.caching.objects;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.CacheObject;

@SuppressWarnings("serial")
public class PlotObject extends CacheObject<PlotId>{

		public PlotObject(PlotId id) {
			super(id);
		}

		@Override
		public String toString() {
			return object.toString();
		}

		@Override
		public PlotId fromString(String serialized) {
			object = PlotId.fromString(serialized);
			return object;
		}
		
}