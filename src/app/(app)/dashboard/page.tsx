import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Package, AlertTriangle, Boxes, BarChart3 } from 'lucide-react';
import { ChartContainer, ChartTooltip, ChartTooltipContent, BarChart, XAxis, YAxis, Bar } from '@/components/ui/chart';

const chartData = [
  { month: 'January', shipped: 186, received: 80 },
  { month: 'February', shipped: 305, received: 200 },
  { month: 'March', shipped: 237, received: 120 },
  { month: 'April', shipped: 273, received: 190 },
  { month: 'May', shipped: 209, received: 130 },
  { month: 'June', shipped: 214, received: 140 },
];

const chartConfig = {
  shipped: { label: 'Shipped', color: 'hsl(var(--primary))' },
  received: { label: 'Received', color: 'hsl(var(--accent))' },
};

export default function DashboardPage() {
  return (
    <div className="flex flex-col gap-6">
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Inventory</CardTitle>
            <Package className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">1,234,567 units</div>
            <p className="text-xs text-muted-foreground">+20.1% from last month</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Orders Pending</CardTitle>
            <Boxes className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">245</div>
            <p className="text-xs text-muted-foreground">+15 since last hour</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Low Stock Alerts</CardTitle>
            <AlertTriangle className="h-4 w-4 text-destructive" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">12 items</div>
            <p className="text-xs text-muted-foreground">Needs immediate attention</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Warehouse Capacity</CardTitle>
            <BarChart3 className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">85%</div>
            <p className="text-xs text-muted-foreground">Optimal capacity utilization</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Inventory Turnover</CardTitle>
        </CardHeader>
        <CardContent>
          <ChartContainer config={chartConfig} className="h-[300px] w-full">
            <BarChart accessibilityLayer data={chartData}>
              <XAxis dataKey="month" tickLine={false} tickMargin={10} axisLine={false} />
              <YAxis />
              <ChartTooltip content={<ChartTooltipContent />} />
              <Bar dataKey="shipped" fill="var(--color-shipped)" radius={4} />
              <Bar dataKey="received" fill="var(--color-received)" radius={4} />
            </BarChart>
          </ChartContainer>
        </CardContent>
      </Card>
    </div>
  );
}
