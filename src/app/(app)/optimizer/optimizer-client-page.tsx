'use client';

import React, { useState } from 'react';
import { useForm, useFieldArray } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import type { OptimizerFormState } from './actions';
import { runOptimizer } from './actions';

import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Trash2, PlusCircle, Bot, Loader2 } from 'lucide-react';

const formSchema = z.object({
  items: z.array(z.object({
    id: z.string().min(1, 'Product ID is required'),
    stock: z.coerce.number().min(0, 'Must be positive'),
    width: z.coerce.number().min(0, 'Must be positive'),
    height: z.coerce.number().min(0, 'Must be positive'),
    depth: z.coerce.number().min(0, 'Must be positive'),
    frequency: z.coerce.number().min(0, 'Must be positive'),
  })).min(1, 'At least one item is required.'),
  currentLayout: z.string().optional(),
});

type FormData = z.infer<typeof formSchema>;

const initialFormState: OptimizerFormState = { data: null, error: null };

export default function OptimizerClientPage() {
    const [state, setState] = useState<OptimizerFormState>(initialFormState);
    const [isPending, setIsPending] = useState(false);

    const form = useForm<FormData>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            items: [
                { id: 'ASP-001', stock: 5000, width: 10, height: 5, depth: 5, frequency: 150 },
                { id: 'IBU-002', stock: 8000, width: 12, height: 6, depth: 6, frequency: 200 },
                { id: 'PARA-003', stock: 12000, width: 8, height: 4, depth: 4, frequency: 300 },
            ],
            currentLayout: 'Standard racking system with 5 aisles. High-frequency items are currently scattered.'
        },
    });

    const { fields, append, remove } = useFieldArray({
        control: form.control,
        name: "items"
    });

    const onSubmit = async (data: FormData) => {
        setIsPending(true);
        setState(initialFormState);
        const formData = new FormData();
        formData.append('items', JSON.stringify(data.items));
        if (data.currentLayout) {
          formData.append('currentLayout', data.currentLayout);
        }

        const result = await runOptimizer(initialFormState, formData);
        
        setState(result);
        setIsPending(false);
    };

    return (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 items-start">
            <Card className="lg:col-span-2">
                <form onSubmit={form.handleSubmit(onSubmit)}>
                    <CardHeader>
                        <CardTitle>Warehouse Data Input</CardTitle>
                        <CardDescription>
                            Enter details about your inventory and current layout to get an optimized plan.
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-6">
                        <div className="space-y-4">
                           <Label>Inventory Items</Label>
                           <div className="space-y-4">
                            {fields.map((field, index) => (
                                <Card key={field.id} className="p-4 bg-background/50">
                                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                                        <div className="col-span-2 md:col-span-1">
                                            <Label htmlFor={`items.${index}.id`}>Product ID</Label>
                                            <Input {...form.register(`items.${index}.id`)} className="mt-1" />
                                            {form.formState.errors.items?.[index]?.id && <p className="text-destructive text-xs mt-1">{form.formState.errors.items[index].id.message}</p>}
                                        </div>
                                        <div>
                                            <Label htmlFor={`items.${index}.stock`}>Stock</Label>
                                            <Input type="number" {...form.register(`items.${index}.stock`)} className="mt-1" />
                                        </div>
                                        <div>
                                            <Label htmlFor={`items.${index}.frequency`}>Order Freq.</Label>
                                            <Input type="number" {...form.register(`items.${index}.frequency`)} className="mt-1" />
                                        </div>
                                        <div className="flex items-end justify-end">
                                            <Button type="button" variant="ghost" size="icon" className="text-destructive hover:bg-destructive/10" onClick={() => remove(index)}>
                                                <Trash2 className="h-4 w-4" />
                                            </Button>
                                        </div>
                                        <div className="col-span-2 md:col-span-4 grid grid-cols-3 gap-4">
                                            <div>
                                                <Label htmlFor={`items.${index}.width`}>Width (cm)</Label>
                                                <Input type="number" {...form.register(`items.${index}.width`)} className="mt-1" />
                                            </div>
                                            <div>
                                                <Label htmlFor={`items.${index}.height`}>Height (cm)</Label>
                                                <Input type="number" {...form.register(`items.${index}.height`)} className="mt-1" />
                                            </div>
                                            <div>
                                                <Label htmlFor={`items.${index}.depth`}>Depth (cm)</Label>
                                                <Input type="number" {...form.register(`items.${index}.depth`)} className="mt-1" />
                                            </div>
                                        </div>
                                    </div>
                                </Card>
                            ))}
                            </div>
                             {form.formState.errors.items && <p className="text-destructive text-xs mt-1">{form.formState.errors.items.message}</p>}
                            <Button type="button" variant="outline" size="sm" onClick={() => append({ id: '', stock: 0, width: 0, height: 0, depth: 0, frequency: 0 })}>
                                <PlusCircle className="mr-2 h-4 w-4" />
                                Add Item
                            </Button>
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="currentLayout">Current Layout Description (Optional)</Label>
                            <Textarea id="currentLayout" {...form.register('currentLayout')} placeholder="e.g., Aisle A contains..., high-traffic items are near the front..." />
                        </div>
                    </CardContent>
                    <CardFooter>
                       <Button type="submit" disabled={isPending} className="w-full sm:w-auto">
                            {isPending ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : <Bot className="mr-2 h-4 w-4" />}
                            Optimize Layout
                        </Button>
                    </CardFooter>
                </form>
            </Card>

            <div className="space-y-6">
              <Card className="sticky top-20">
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Bot className="text-primary"/> AI Suggestion
                    </CardTitle>
                </CardHeader>
                <CardContent className="space-y-4 min-h-[200px]">
                    {isPending && <div className="flex justify-center items-center h-full"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>}
                    {!isPending && state.error && <p className="text-destructive">{state.error}</p>}
                    
                    {!isPending && state.data ? (
                        <div className="space-y-4">
                            <div>
                                <h3 className="font-semibold mb-2 text-primary">Optimized Layout</h3>
                                <p className="text-sm text-muted-foreground whitespace-pre-wrap">{state.data.optimizedLayout}</p>
                            </div>
                            <hr />
                            <div>
                                <h3 className="font-semibold mb-2 text-primary">Estimated Improvement</h3>
                                <p className="text-sm text-muted-foreground whitespace-pre-wrap">{state.data.estimatedImprovement}</p>
                            </div>
                        </div>
                    ) : (
                         !isPending && !state.error && <p className="text-sm text-muted-foreground">Submit your warehouse data to receive an optimized layout suggestion and efficiency improvements.</p>
                    )}
                </CardContent>
              </Card>
            </div>
        </div>
    );
}
