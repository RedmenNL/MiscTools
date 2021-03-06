package redmennl.mods.mito.client.renderer.item;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import redmennl.mods.mito.client.model.ModelPortableHouse;
import redmennl.mods.mito.lib.Resources;

import com.google.common.primitives.SignedBytes;

import cpw.mods.fml.client.FMLClientHandler;

public class ItemRendererPortableHouse implements IItemRenderer
{
    private Random random;
    private RenderBlocks itemRenderBlocks = new RenderBlocks();

    private static float[][] shifts = { { 0.3F, 0.7F, 0.3F },
            { 0.7F, 0.7F, 0.3F }, { 0.3F, 0.7F, 0.7F }, { 0.7F, 0.7F, 0.7F },
            { 0.3F, 0.3F, 0.3F }, { 0.7F, 0.3F, 0.3F }, { 0.3F, 0.3F, 0.7F },
            { 0.7F, 0.3F, 0.7F }, { 0.5F, 0.5F, 0.5F } };

    public ItemRendererPortableHouse()
    {
        model = new ModelPortableHouse();
        random = new Random();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
            ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch (type)
        {
            case ENTITY:
            {
                renderPortableHouse(-0.5F, 0.0F, -0.5F, 0.5F, item);
                return;
            }
            case EQUIPPED:
            {
                renderPortableHouse(0.0F, 0.0F, 0.0F, 0.5F, item);
                return;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                renderPortableHouse(0.5F, 0.5F, 0.3F, 0.5F, item);
                return;
            }
            case INVENTORY:
            {
                renderPortableHouse(0.5F, 0.3F, 0.5F, 0.5F, item);
                return;
            }
            default:
                return;
        }
    }

    public byte getMiniBlockCount(ItemStack stack)
    {
        return SignedBytes
                .saturatedCast(Math.min(stack.stackSize / 32, 15) + 1);
    }

    private void renderPortableHouse(float x, float y, float z, float scale,
            ItemStack i)
    {

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        // Render frame
        GL11.glPushMatrix();

        if (i.getItemDamage() == 0 || i.getItemDamage() == 1)
        {
            FMLClientHandler.instance().getClient().renderEngine
                    .func_110577_a(Resources.MODEL_TEXTURE_PORTABLEHOUSE);
        }
        if (i.getItemDamage() == 2 || i.getItemDamage() == 3)
        {
            FMLClientHandler.instance().getClient().renderEngine
                    .func_110577_a(Resources.MODEL_TEXTURE_ADVANCEDPORTABLEHOUSE);
        }

        GL11.glTranslatef(x + 0.5F, y + 1.5F, z + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);

        model.renderFrame();

        GL11.glPopMatrix();

        if (i.hasTagCompound())
        {

            random.setSeed(254L);
            float shiftX;
            float shiftY;
            float shiftZ;
            int shift = 0;
            float blockScale = 0.70F;
            float timeD = (float) (360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
            int[] idArr = new int[9 * 9 * 5];
            int[] metaArr = new int[9 * 9 * 5];
            idArr = i.getTagCompound().getIntArray("id");
            metaArr = i.getTagCompound().getIntArray("meta");

            GL11.glPushMatrix();

            GL11.glTranslatef(x, y, z);
            for (int o = 0; o < idArr.length; o++)
            {
                int id = idArr[o];
                int meta = metaArr[o];

                if (id == 0)
                {
                    continue;
                }
                if (shift >= shifts.length)
                {
                    break;
                }
                ItemStack item = new ItemStack(id, 1, meta);
                shiftX = shifts[shift][0];
                shiftY = shifts[shift][1];
                shiftZ = shifts[shift][2];
                shift++;
                GL11.glPushMatrix();
                GL11.glTranslatef(shiftX, shiftY, shiftZ);
                GL11.glRotatef(timeD, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(blockScale, blockScale, blockScale);

                byte b0 = getMiniBlockCount(item);
                float f4;
                float f5;
                float f6;
                Block block = null;
                if (item.itemID < Block.blocksList.length)
                {
                    block = Block.blocksList[item.itemID];
                }
                if (block != null /*
                                   * && item.getItemSpriteNumber() == 0 &&
                                   * RenderBlocks
                                   * .renderItemIn3d(Block.blocksList
                                   * [item.itemID].getRenderType())
                                   */)
                {
                    GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

                    // FMLClientHandler.instance().getClient().renderEngine.func_110577_a("/terrain.png");
                    if (item.getItemSpriteNumber() == 0)
                    {
                        FMLClientHandler.instance().getClient().renderEngine
                                .func_110577_a(TextureMap.field_110575_b);
                    } else
                    {
                        FMLClientHandler.instance().getClient().renderEngine
                                .func_110577_a(TextureMap.field_110576_c);
                    }
                    float f7 = 0.25F;
                    int j = block.getRenderType();

                    if (j == 1 || j == 19 || j == 12 || j == 2)
                    {
                        f7 = 0.5F;
                    }

                    GL11.glScalef(f7, f7, f7);

                    int p;
                    for (p = 0; p < b0; ++p)
                    {
                        GL11.glPushMatrix();

                        if (p > 0)
                        {
                            f5 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                            f4 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                            f6 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                            GL11.glTranslatef(f5, f4, f6);
                        }

                        f5 = 1.0F;
                        itemRenderBlocks.renderBlockAsItem(block,
                                item.getItemDamage(), f5);
                        GL11.glPopMatrix();
                    }
                } else
                {
                    System.out.println("Can't render!");
                }
                GL11.glPopMatrix();

            }

            GL11.glPopMatrix();

        }

        // Render middle
        float pulse = 0.1F + (float) (System.currentTimeMillis() % 2000) / 8000;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();

        FMLClientHandler.instance().getClient().renderEngine
                .func_110577_a(Resources.MODEL_TEXTURE_PORTABLEHOUSE);
        GL11.glColor4f(1F, 1F, 1F, pulse);
        GL11.glTranslatef(x + 0.5F, y + 1.5F, z + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);
        model.renderMiddle();
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private ModelPortableHouse model;

}
