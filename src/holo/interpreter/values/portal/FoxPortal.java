package holo.interpreter.values.portal;

public interface FoxPortal {
	
	public static FoxPortalWrapper of(Object portal) {
		return new FoxPortalWrapper(portal);
	}
	
	public String typeName();
	
}
