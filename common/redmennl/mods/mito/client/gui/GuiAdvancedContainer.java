package redmennl.mods.mito.client.gui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import redmennl.mods.mito.inventory.slots.AdvancedSlot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiAdvancedContainer extends GuiScreen
{
    protected static final ResourceLocation field_110408_a = new ResourceLocation(
            "textures/gui/container/inventory.png");

    /** Stacks renderer. Icons, stack size, health, etc... */
    protected static RenderItem itemRenderer = new RenderItem();

    /** The X size of the inventory window in pixels. */
    protected int xSize = 176;

    /** The Y size of the inventory window in pixels. */
    protected int ySize = 166;

    /** A list of the players inventory slots. */
    public Container inventorySlots;

    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiLeft;

    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiTop;
    private Slot theSlot;

    /** Used when touchscreen is enabled */
    private Slot clickedSlot;

    /** Used when touchscreen is enabled */
    private boolean isRightMouseClick;

    /** Used when touchscreen is enabled */
    private ItemStack draggedStack;
    private int field_85049_r;
    private int field_85048_s;
    private Slot returningStackDestSlot;
    private long returningStackTime;

    /** Used when touchscreen is enabled */
    private ItemStack returningStack;
    private Slot field_92033_y;
    private long field_92032_z;
    @SuppressWarnings("rawtypes")
    protected final Set field_94077_p = new HashSet();
    protected boolean field_94076_q;
    private int field_94071_C;
    private int field_94067_D;
    private boolean field_94068_E;
    private int field_94069_F;
    private long field_94070_G;
    private Slot field_94072_H;
    private int field_94073_I;
    private boolean field_94074_J;
    private ItemStack field_94075_K;

    public GuiAdvancedContainer(Container par1Container)
    {
        inventorySlots = par1Container;
        field_94068_E = true;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        super.initGui();
        mc.thePlayer.openContainer = inventorySlots;
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        int k = guiLeft;
        int l = guiTop;
        this.drawGuiContainerBackgroundLayer(par3, par1, par2);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        super.drawScreen(par1, par2, par3);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef(k, l, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        theSlot = null;
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
                short1 / 1.0F, short2 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i1;

        for (int j1 = 0; j1 < inventorySlots.inventorySlots.size(); ++j1)
        {
            AdvancedSlot slot = (AdvancedSlot) inventorySlots.inventorySlots
                    .get(j1);
            if (slot.isVisible())
            {
                this.drawSlotInventory(slot);

                if (this.isMouseOverSlot(slot, par1, par2)
                        && slot.func_111238_b())
                {
                    theSlot = slot;
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    int k1 = slot.xDisplayPosition;
                    i1 = slot.yDisplayPosition;
                    this.drawGradientRect(k1, i1, k1 + 16, i1 + 16,
                            -2130706433, -2130706433);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                }
            }
        }

        // Forge: Force lighting to be disabled as there are some issue where
        // lighting would
        // incorrectly be applied based on items that are in the inventory.
        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawGuiContainerForegroundLayer(par1, par2);
        GL11.glEnable(GL11.GL_LIGHTING);
        InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
        ItemStack itemstack = draggedStack == null ? inventoryplayer
                .getItemStack() : draggedStack;

        if (itemstack != null)
        {
            byte b0 = 8;
            i1 = draggedStack == null ? 8 : 16;
            String s = null;

            if (draggedStack != null && isRightMouseClick)
            {
                itemstack = itemstack.copy();
                itemstack.stackSize = MathHelper
                        .ceiling_float_int(itemstack.stackSize / 2.0F);
            } else if (field_94076_q && field_94077_p.size() > 1)
            {
                itemstack = itemstack.copy();
                itemstack.stackSize = field_94069_F;

                if (itemstack.stackSize == 0)
                {
                    s = "" + EnumChatFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemstack, par1 - k - b0, par2 - l - i1, s);
        }

        if (returningStack != null)
        {
            float f1 = (Minecraft.getSystemTime() - returningStackTime) / 100.0F;

            if (f1 >= 1.0F)
            {
                f1 = 1.0F;
                returningStack = null;
            }

            i1 = returningStackDestSlot.xDisplayPosition - field_85049_r;
            int l1 = returningStackDestSlot.yDisplayPosition - field_85048_s;
            int i2 = field_85049_r + (int) (i1 * f1);
            int j2 = field_85048_s + (int) (l1 * f1);
            this.drawItemStack(returningStack, i2, j2, (String) null);
        }

        GL11.glPopMatrix();

        if (inventoryplayer.getItemStack() == null && theSlot != null
                && theSlot.getHasStack())
        {
            ItemStack itemstack1 = theSlot.getStack();
            this.drawItemStackTooltip(itemstack1, par1, par2);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    private void drawItemStack(ItemStack par1ItemStack, int par2, int par3,
            String par4Str)
    {
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        zLevel = 200.0F;
        itemRenderer.zLevel = 200.0F;
        FontRenderer font = null;
        if (par1ItemStack != null)
        {
            font = par1ItemStack.getItem().getFontRenderer(par1ItemStack);
        }
        if (font == null)
        {
            font = fontRenderer;
        }
        itemRenderer.renderItemAndEffectIntoGUI(font, mc.func_110434_K(),
                par1ItemStack, par2, par3);
        itemRenderer.renderItemOverlayIntoGUI(font, mc.func_110434_K(),
                par1ItemStack, par2, par3 - (draggedStack == null ? 0 : 8),
                par4Str);
        zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void drawItemStackTooltip(ItemStack par1ItemStack, int par2,
            int par3)
    {
        List list = par1ItemStack.getTooltip(mc.thePlayer,
                mc.gameSettings.advancedItemTooltips);

        for (int k = 0; k < list.size(); ++k)
        {
            if (k == 0)
            {
                list.set(
                        k,
                        "\u00a7"
                                + Integer.toHexString(par1ItemStack.getRarity().rarityColor)
                                + (String) list.get(k));
            } else
            {
                list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
            }
        }

        FontRenderer font = par1ItemStack.getItem().getFontRenderer(
                par1ItemStack);
        drawHoveringText(list, par2, par3, font == null ? fontRenderer : font);
    }

    /**
     * Draws the text when mouse is over creative inventory tab. Params: current
     * creative tab to be checked, current mouse x position, current mouse y
     * position.
     */
    protected void drawCreativeTabHoveringText(String par1Str, int par2,
            int par3)
    {
        this.func_102021_a(Arrays.asList(new String[] { par1Str }), par2, par3);
    }

    @SuppressWarnings("rawtypes")
    protected void func_102021_a(List par1List, int par2, int par3)
    {
        drawHoveringText(par1List, par2, par3, fontRenderer);
    }

    @SuppressWarnings("rawtypes")
    protected void drawHoveringText(List par1List, int par2, int par3,
            FontRenderer font)
    {
        if (!par1List.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = par1List.iterator();

            while (iterator.hasNext())
            {
                String s = (String) iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int i1 = par2 + 12;
            int j1 = par3 - 12;
            int k1 = 8;

            if (par1List.size() > 1)
            {
                k1 += 2 + (par1List.size() - 1) * 10;
            }

            if (i1 + k > width)
            {
                i1 -= 28 + k;
            }

            if (j1 + k1 + 6 > height)
            {
                j1 = height - k1 - 6;
            }

            zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int l1 = -267386864;
            this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4,
                    l1, l1);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1,
                    l1);
            this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3,
                    l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3
                    - 1, i2, j2);
            this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1
                    + 3 - 1, i2, j2);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2,
                    i2);
            this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3,
                    j2, j2);

            for (int k2 = 0; k2 < par1List.size(); ++k2)
            {
                String s1 = (String) par1List.get(k2);
                font.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0)
                {
                    j1 += 2;
                }

                j1 += 10;
            }

            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    protected abstract void drawGuiContainerBackgroundLayer(float f, int i,
            int j);

    /**
     * Draws an inventory slot
     */
    protected void drawSlotInventory(Slot par1Slot)
    {
        int i = par1Slot.xDisplayPosition;
        int j = par1Slot.yDisplayPosition;
        ItemStack itemstack = par1Slot.getStack();
        boolean flag = false;
        boolean flag1 = par1Slot == clickedSlot && draggedStack != null
                && !isRightMouseClick;
        ItemStack itemstack1 = mc.thePlayer.inventory.getItemStack();
        String s = null;

        if (par1Slot == clickedSlot && draggedStack != null
                && isRightMouseClick && itemstack != null)
        {
            itemstack = itemstack.copy();
            itemstack.stackSize /= 2;
        } else if (field_94076_q && field_94077_p.contains(par1Slot)
                && itemstack1 != null)
        {
            if (field_94077_p.size() == 1)
                return;

            if (Container.func_94527_a(par1Slot, itemstack1, true)
                    && inventorySlots.func_94531_b(par1Slot))
            {
                itemstack = itemstack1.copy();
                flag = true;
                Container.func_94525_a(field_94077_p, field_94071_C, itemstack,
                        par1Slot.getStack() == null ? 0
                                : par1Slot.getStack().stackSize);

                if (itemstack.stackSize > itemstack.getMaxStackSize())
                {
                    s = EnumChatFormatting.YELLOW + ""
                            + itemstack.getMaxStackSize();
                    itemstack.stackSize = itemstack.getMaxStackSize();
                }

                if (itemstack.stackSize > par1Slot.getSlotStackLimit())
                {
                    s = EnumChatFormatting.YELLOW + ""
                            + par1Slot.getSlotStackLimit();
                    itemstack.stackSize = par1Slot.getSlotStackLimit();
                }
            } else
            {
                field_94077_p.remove(par1Slot);
                this.func_94066_g();
            }
        }

        zLevel = 100.0F;
        itemRenderer.zLevel = 100.0F;

        if (itemstack == null)
        {
            Icon icon = par1Slot.getBackgroundIconIndex();

            if (icon != null)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                mc.func_110434_K().func_110577_a(TextureMap.field_110576_c);
                this.drawTexturedModelRectFromIcon(i, j, icon, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                flag1 = true;
            }
        }

        if (!flag1)
        {
            if (flag)
            {
                drawRect(i, j, i + 16, j + 16, -2130706433);
            }

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            itemRenderer.renderItemAndEffectIntoGUI(fontRenderer,
                    mc.func_110434_K(), itemstack, i, j);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer,
                    mc.func_110434_K(), itemstack, i, j, s);
        }

        itemRenderer.zLevel = 0.0F;
        zLevel = 0.0F;
    }

    @SuppressWarnings("rawtypes")
    private void func_94066_g()
    {
        ItemStack itemstack = mc.thePlayer.inventory.getItemStack();

        if (itemstack != null && field_94076_q)
        {
            field_94069_F = itemstack.stackSize;
            ItemStack itemstack1;
            int i;

            for (Iterator iterator = field_94077_p.iterator(); iterator
                    .hasNext(); field_94069_F -= itemstack1.stackSize - i)
            {
                Slot slot = (Slot) iterator.next();
                itemstack1 = itemstack.copy();
                i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
                Container.func_94525_a(field_94077_p, field_94071_C,
                        itemstack1, i);

                if (itemstack1.stackSize > itemstack1.getMaxStackSize())
                {
                    itemstack1.stackSize = itemstack1.getMaxStackSize();
                }

                if (itemstack1.stackSize > slot.getSlotStackLimit())
                {
                    itemstack1.stackSize = slot.getSlotStackLimit();
                }
            }
        }
    }

    /**
     * Returns the slot at the given coordinates or null if there is none.
     */
    private AdvancedSlot getSlotAtPosition(int par1, int par2)
    {
        for (int k = 0; k < inventorySlots.inventorySlots.size(); ++k)
        {
            AdvancedSlot slot = (AdvancedSlot) inventorySlots.inventorySlots
                    .get(k);

            if (this.isMouseOverSlot(slot, par1, par2))
                return slot;
        }

        return null;
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        boolean flag = par3 == mc.gameSettings.keyBindPickBlock.keyCode + 100;
        Slot slot = this.getSlotAtPosition(par1, par2);
        long l = Minecraft.getSystemTime();
        field_94074_J = field_94072_H == slot && l - field_94070_G < 250L
                && field_94073_I == par3;
        field_94068_E = false;

        if (par3 == 0 || par3 == 1 || flag)
        {
            int i1 = guiLeft;
            int j1 = guiTop;
            boolean flag1 = par1 < i1 || par2 < j1 || par1 >= i1 + xSize
                    || par2 >= j1 + ySize;
            int k1 = -1;

            if (slot != null)
            {
                k1 = slot.slotNumber;
            }

            if (flag1)
            {
                k1 = -999;
            }

            if (mc.gameSettings.touchscreen && flag1
                    && mc.thePlayer.inventory.getItemStack() == null)
            {
                mc.displayGuiScreen((GuiScreen) null);
                return;
            }

            if (k1 != -1)
            {
                if (mc.gameSettings.touchscreen)
                {
                    if (slot != null && slot.getHasStack())
                    {
                        clickedSlot = slot;
                        draggedStack = null;
                        isRightMouseClick = par3 == 1;
                    } else
                    {
                        clickedSlot = null;
                    }
                } else if (!field_94076_q)
                {
                    if (mc.thePlayer.inventory.getItemStack() == null)
                    {
                        if (par3 == mc.gameSettings.keyBindPickBlock.keyCode + 100)
                        {
                            this.handleMouseClick(slot, k1, par3, 3);
                        } else
                        {
                            boolean flag2 = k1 != -999
                                    && (Keyboard.isKeyDown(42) || Keyboard
                                            .isKeyDown(54));
                            byte b0 = 0;

                            if (flag2)
                            {
                                field_94075_K = slot != null
                                        && slot.getHasStack() ? slot.getStack()
                                        : null;
                                b0 = 1;
                            } else if (k1 == -999)
                            {
                                b0 = 4;
                            }

                            this.handleMouseClick(slot, k1, par3, b0);
                        }

                        field_94068_E = true;
                    } else
                    {
                        field_94076_q = true;
                        field_94067_D = par3;
                        field_94077_p.clear();

                        if (par3 == 0)
                        {
                            field_94071_C = 0;
                        } else if (par3 == 1)
                        {
                            field_94071_C = 1;
                        }
                    }
                }
            }
        }

        field_94072_H = slot;
        field_94070_G = l;
        field_94073_I = par3;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void func_85041_a(int par1, int par2, int par3, long par4)
    {
        AdvancedSlot slot = this.getSlotAtPosition(par1, par2);
        ItemStack itemstack = mc.thePlayer.inventory.getItemStack();

        if (clickedSlot != null && mc.gameSettings.touchscreen)
        {
            if (par3 == 0 || par3 == 1)
            {
                if (draggedStack == null)
                {
                    if (slot != clickedSlot)
                    {
                        draggedStack = clickedSlot.getStack().copy();
                    }
                } else if (draggedStack.stackSize > 1 && slot != null
                        && Container.func_94527_a(slot, draggedStack, false))
                {
                    long i1 = Minecraft.getSystemTime();

                    if (field_92033_y == slot)
                    {
                        if (i1 - field_92032_z > 500L)
                        {
                            this.handleMouseClick(clickedSlot,
                                    clickedSlot.slotNumber, 0, 0);
                            this.handleMouseClick(slot, slot.slotNumber, 1, 0);
                            this.handleMouseClick(clickedSlot,
                                    clickedSlot.slotNumber, 0, 0);
                            field_92032_z = i1 + 750L;
                            --draggedStack.stackSize;
                        }
                    } else
                    {
                        field_92033_y = slot;
                        field_92032_z = i1;
                    }
                }
            }
        } else if (field_94076_q && slot != null && itemstack != null
                && itemstack.stackSize > field_94077_p.size()
                && Container.func_94527_a(slot, itemstack, true)
                && slot.isItemValid(itemstack)
                && inventorySlots.func_94531_b(slot) && slot.isVisible())
        {
            field_94077_p.add(slot);
            this.func_94066_g();
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released. Signature:
     * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
     * mouseUp
     */
    @Override
    @SuppressWarnings("rawtypes")
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        AdvancedSlot slot = this.getSlotAtPosition(par1, par2);
        int l = guiLeft;
        int i1 = guiTop;
        boolean flag = par1 < l || par2 < i1 || par1 >= l + xSize
                || par2 >= i1 + ySize;
        int j1 = -1;

        if (slot != null)
        {
            j1 = slot.slotNumber;
        }

        if (flag)
        {
            j1 = -999;
        }

        Slot slot1;
        Iterator iterator;

        if (field_94074_J && slot != null && par3 == 0
                && inventorySlots.func_94530_a((ItemStack) null, slot)
                && slot.isVisible())
        {
            if (isShiftKeyDown())
            {
                if (slot != null && slot.inventory != null
                        && field_94075_K != null)
                {
                    iterator = inventorySlots.inventorySlots.iterator();

                    while (iterator.hasNext())
                    {
                        slot1 = (Slot) iterator.next();

                        if (slot1 != null
                                && slot1.canTakeStack(mc.thePlayer)
                                && slot1.getHasStack()
                                && slot1.inventory == slot.inventory
                                && Container.func_94527_a(slot1, field_94075_K,
                                        true))
                        {
                            this.handleMouseClick(slot1, slot1.slotNumber,
                                    par3, 1);
                        }
                    }
                }
            } else
            {
                this.handleMouseClick(slot, j1, par3, 6);
            }

            field_94074_J = false;
            field_94070_G = 0L;
        } else
        {
            if (field_94076_q && field_94067_D != par3)
            {
                field_94076_q = false;
                field_94077_p.clear();
                field_94068_E = true;
                return;
            }

            if (field_94068_E)
            {
                field_94068_E = false;
                return;
            }

            boolean flag1;

            if (clickedSlot != null && mc.gameSettings.touchscreen)
            {
                if (par3 == 0 || par3 == 1)
                {
                    if (draggedStack == null && slot != clickedSlot)
                    {
                        draggedStack = clickedSlot.getStack();
                    }

                    flag1 = Container.func_94527_a(slot, draggedStack, false);

                    if (j1 != -1 && draggedStack != null && flag1)
                    {
                        this.handleMouseClick(clickedSlot,
                                clickedSlot.slotNumber, par3, 0);
                        this.handleMouseClick(slot, j1, 0, 0);

                        if (mc.thePlayer.inventory.getItemStack() != null)
                        {
                            this.handleMouseClick(clickedSlot,
                                    clickedSlot.slotNumber, par3, 0);
                            field_85049_r = par1 - l;
                            field_85048_s = par2 - i1;
                            returningStackDestSlot = clickedSlot;
                            returningStack = draggedStack;
                            returningStackTime = Minecraft.getSystemTime();
                        } else
                        {
                            returningStack = null;
                        }
                    } else if (draggedStack != null)
                    {
                        field_85049_r = par1 - l;
                        field_85048_s = par2 - i1;
                        returningStackDestSlot = clickedSlot;
                        returningStack = draggedStack;
                        returningStackTime = Minecraft.getSystemTime();
                    }

                    draggedStack = null;
                    clickedSlot = null;
                }
            } else if (field_94076_q && !field_94077_p.isEmpty())
            {
                this.handleMouseClick((Slot) null, -999,
                        Container.func_94534_d(0, field_94071_C), 5);
                iterator = field_94077_p.iterator();

                while (iterator.hasNext())
                {
                    slot1 = (Slot) iterator.next();
                    this.handleMouseClick(slot1, slot1.slotNumber,
                            Container.func_94534_d(1, field_94071_C), 5);
                }

                this.handleMouseClick((Slot) null, -999,
                        Container.func_94534_d(2, field_94071_C), 5);
            } else if (mc.thePlayer.inventory.getItemStack() != null
                    && slot != null && slot.isVisible())
            {
                if (par3 == mc.gameSettings.keyBindPickBlock.keyCode + 100)
                {
                    this.handleMouseClick(slot, j1, par3, 3);
                } else
                {
                    flag1 = j1 != -999
                            && (Keyboard.isKeyDown(42) || Keyboard
                                    .isKeyDown(54));

                    if (flag1)
                    {
                        field_94075_K = slot != null && slot.getHasStack() ? slot
                                .getStack() : null;
                    }

                    this.handleMouseClick(slot, j1, par3, flag1 ? 1 : 0);
                }
            }
        }

        if (mc.thePlayer.inventory.getItemStack() == null)
        {
            field_94070_G = 0L;
        }

        field_94076_q = false;
    }

    /**
     * Returns if the passed mouse position is over the specified slot.
     */
    private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3)
    {
        return this.isPointInRegion(par1Slot.xDisplayPosition,
                par1Slot.yDisplayPosition, 16, 16, par2, par3);
    }

    /**
     * Args: left, top, width, height, pointX, pointY. Note: left, top are local
     * to Gui, pointX, pointY are local to screen
     */
    protected boolean isPointInRegion(int par1, int par2, int par3, int par4,
            int par5, int par6)
    {
        int k1 = guiLeft;
        int l1 = guiTop;
        par5 -= k1;
        par6 -= l1;
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1
                && par6 < par2 + par4 + 1;
    }

    protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
    {
        if (par1Slot != null)
        {
            par2 = par1Slot.slotNumber;
        }

        mc.playerController.windowClick(inventorySlots.windowId, par2, par3,
                par4, mc.thePlayer);
    }

    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1 || par2 == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.thePlayer.closeScreen();
        }

        this.checkHotbarKeys(par2);

        if (theSlot != null && theSlot.getHasStack())
        {
            if (par2 == mc.gameSettings.keyBindPickBlock.keyCode)
            {
                this.handleMouseClick(theSlot, theSlot.slotNumber, 0, 3);
            } else if (par2 == mc.gameSettings.keyBindDrop.keyCode)
            {
                this.handleMouseClick(theSlot, theSlot.slotNumber,
                        isCtrlKeyDown() ? 1 : 0, 4);
            }
        }
    }

    /**
     * This function is what controls the hotbar shortcut check when you press a
     * number key when hovering a stack.
     */
    protected boolean checkHotbarKeys(int par1)
    {
        if (mc.thePlayer.inventory.getItemStack() == null && theSlot != null)
        {
            for (int j = 0; j < 9; ++j)
            {
                if (par1 == 2 + j)
                {
                    this.handleMouseClick(theSlot, theSlot.slotNumber, j, 2);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat
     * events
     */
    @Override
    public void onGuiClosed()
    {
        if (mc.thePlayer != null)
        {
            inventorySlots.onCraftGuiClosed(mc.thePlayer);
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in
     * single-player
     */
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
    }
}
