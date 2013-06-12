package redmennl.mods.mito.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import redmennl.mods.mito.lib.Library;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHelmetTorch extends ItemMiTo
{

	public ItemHelmetTorch(int id)
	{
		super(id);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setUnlocalizedName("helmetTorch");
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		this.itemIcon = ir.registerIcon(Library.MOD_ID + ":" + this.getUnlocalizedName().substring(5));
	}
}