package richbar.com.github.commandplot.caching.nbt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;
import org.jnbt.Tag;

import richbar.com.github.commandplot.caching.CacheBackend;
import richbar.com.github.commandplot.caching.CacheObject;

public class NBTCache implements CacheBackend<Tag> {

	CompoundTag compound;
	private String compoundName;
	private final File file;
	
	public NBTCache(File file, String compoundName) {
		this.file = file;
		this.compoundName = compoundName;
		loadFromBackend();
	}
	
	@Override
	public boolean remove(CacheObject<Tag> elem) {
		return remove(elem.getObject());
	}

	@Override
	public boolean addObject(CacheObject<Tag> elem) {
		return addObject(elem.getObject());
	}

	@Override
	public boolean contains(CacheObject<Tag> elem) {
		return contains(elem.getObject());
	}
	
	public boolean remove(Tag elem) {
		return write() && compound.getValue().remove(elem.getName(), elem);
	}

	public boolean addObject(Tag elem) {
		return write() && compound.getValue().put(elem.getName(), elem) != null;
	}

	public boolean contains(Tag elem) {
		return compound.getValue().containsValue(elem);
	}

	@Override
	public void loadFromBackend() {
		try{
			if (!file.exists())
			{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileInputStream fileinputstream = new FileInputStream(file);
			NBTInputStream stream = new NBTInputStream(fileinputstream);
			Tag tag = stream.readTag();
			if(tag instanceof CompoundTag){
				compound = (CompoundTag) tag;
			}else{
				compound = new CompoundTag(compoundName, null);
			}
			stream.close();
	        fileinputstream.close();
		}catch(IOException e){}
	}

	public boolean write(){
		try {
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			NBTOutputStream stream = new NBTOutputStream(fileoutputstream);
			stream.writeTag(compound);
			stream.close();
			fileoutputstream.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
}
