// This file is machine-generated - edit at your own risk!

'use server';

/**
 * @fileOverview An AI agent that optimizes warehouse layout based on stock levels, item dimensions, and order frequency.
 *
 * - optimizeWarehouseLayout - A function that optimizes warehouse layout.
 * - OptimizeWarehouseLayoutInput - The input type for the optimizeWarehouseLayout function.
 * - OptimizeWarehouseLayoutOutput - The return type for the optimizeWarehouseLayout function.
 */

import {ai} from '@/ai/genkit';
import {z} from 'genkit';

const OptimizeWarehouseLayoutInputSchema = z.object({
  stockLevels: z.record(z.number()).describe('A record of stock levels for each item.'),
  itemDimensions: z
    .record(z.object({width: z.number(), height: z.number(), depth: z.number()}))
    .describe('A record of dimensions for each item.'),
  orderFrequency: z.record(z.number()).describe('A record of order frequency for each item.'),
  currentLayout: z
    .string()
    .optional()
    .describe('A description of the current warehouse layout, if available.'),
});
export type OptimizeWarehouseLayoutInput = z.infer<typeof OptimizeWarehouseLayoutInputSchema>;

const OptimizeWarehouseLayoutOutputSchema = z.object({
  optimizedLayout: z
    .string()
    .describe('A detailed description of the optimized warehouse layout.'),
  estimatedImprovement: z
    .string()
    .describe(
      'An estimate of the improvement in efficiency (e.g., reduced travel time) resulting from the optimized layout.'
    ),
});
export type OptimizeWarehouseLayoutOutput = z.infer<typeof OptimizeWarehouseLayoutOutputSchema>;

export async function optimizeWarehouseLayout(
  input: OptimizeWarehouseLayoutInput
): Promise<OptimizeWarehouseLayoutOutput> {
  return optimizeWarehouseLayoutFlow(input);
}

const prompt = ai.definePrompt({
  name: 'optimizeWarehouseLayoutPrompt',
  input: {schema: OptimizeWarehouseLayoutInputSchema},
  output: {schema: OptimizeWarehouseLayoutOutputSchema},
  prompt: `You are an expert warehouse layout optimization consultant.
  Given the following data about a warehouse, suggest an optimized layout to minimize travel time for order pickers and improve overall efficiency.

  Stock Levels: {{stockLevels}}
  Item Dimensions: {{itemDimensions}}
  Order Frequency: {{orderFrequency}}
  Current Layout (if available): {{currentLayout}}

  Consider factors such as item popularity, size, and compatibility when determining the optimal placement of items.
  Provide a detailed description of the optimized layout and an estimate of the improvement in efficiency.
  Format your answer as follows:
  Optimized Layout: [Detailed description of the optimized warehouse layout]
  Estimated Improvement: [An estimate of the improvement in efficiency]`, //handlebars template here
});

const optimizeWarehouseLayoutFlow = ai.defineFlow(
  {
    name: 'optimizeWarehouseLayoutFlow',
    inputSchema: OptimizeWarehouseLayoutInputSchema,
    outputSchema: OptimizeWarehouseLayoutOutputSchema,
  },
  async input => {
    const {output} = await prompt(input);
    return output!;
  }
);
