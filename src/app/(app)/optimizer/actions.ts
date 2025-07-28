'use server';

import { z } from 'zod';
import { optimizeWarehouseLayout, type OptimizeWarehouseLayoutInput, type OptimizeWarehouseLayoutOutput } from '@/ai/flows/optimize-warehouse-layout';

const formSchema = z.object({
  items: z.array(z.object({
    id: z.string().min(1, 'Product ID is required'),
    stock: z.coerce.number().min(0),
    width: z.coerce.number().min(0),
    height: z.coerce.number().min(0),
    depth: z.coerce.number().min(0),
    frequency: z.coerce.number().min(0),
  })),
  currentLayout: z.string().optional(),
});

export type OptimizerFormState = {
  data: OptimizeWarehouseLayoutOutput | null;
  error: string | null;
};

export async function runOptimizer(
  prevState: OptimizerFormState,
  formData: FormData,
): Promise<OptimizerFormState> {
  let parsed;
  try {
    parsed = formSchema.safeParse({
      items: JSON.parse(formData.get('items') as string),
      currentLayout: formData.get('currentLayout') as string,
    });
  } catch (error) {
    return { data: null, error: 'Invalid data format.' };
  }
  

  if (!parsed.success) {
    return { data: null, error: 'Invalid form data.' };
  }

  const { items, currentLayout } = parsed.data;

  const stockLevels: Record<string, number> = {};
  const itemDimensions: Record<string, { width: number; height: number; depth: number; }> = {};
  const orderFrequency: Record<string, number> = {};

  items.forEach(item => {
    stockLevels[item.id] = item.stock;
    itemDimensions[item.id] = { width: item.width, height: item.height, depth: item.depth };
    orderFrequency[item.id] = item.frequency;
  });

  const aiInput: OptimizeWarehouseLayoutInput = {
    stockLevels,
    itemDimensions,
    orderFrequency,
    currentLayout,
  };

  try {
    const result = await optimizeWarehouseLayout(aiInput);
    return { data: result, error: null };
  } catch (e) {
    console.error(e);
    return { data: null, error: 'An error occurred while running the optimizer.' };
  }
}
