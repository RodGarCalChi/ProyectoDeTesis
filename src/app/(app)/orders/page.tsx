import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { PlusCircle, FileText } from 'lucide-react';

const orders = [
  { id: 'ORD001', customer: 'Global Pharma', date: '2023-10-26', status: 'Shipped', amount: 1250.0 },
  { id: 'ORD002', customer: 'MediCare Supplies', date: '2023-10-25', status: 'Processing', amount: 850.5 },
  { id: 'ORD003', customer: 'HealthFirst Inc.', date: '2023-10-25', status: 'Delivered', amount: 2300.0 },
  { id: 'ORD004', customer: 'City Hospital', date: '2023-10-24', status: 'Pending', amount: 450.75 },
  { id: 'ORD005', customer: 'Wellness Pharmacy', date: '2023-10-23', status: 'Shipped', amount: 150.0 },
  { id: 'ORD006', customer: 'Global Pharma', date: '2023-10-22', status: 'Delivered', amount: 3200.0 },
];

const getStatusVariant = (status: string) => {
  switch (status.toLowerCase()) {
    case 'shipped': return 'secondary';
    case 'processing': return 'default';
    case 'pending': return 'destructive';
    case 'delivered': return 'outline';
    default: return 'default';
  }
};

export default function OrdersPage() {
  return (
    <Card>
      <CardHeader className="flex flex-row flex-wrap items-center justify-between gap-2">
        <div>
          <CardTitle>Recent Orders</CardTitle>
          <CardDescription>View and manage all your recent orders.</CardDescription>
        </div>
        <Button size="sm">
          <PlusCircle className="mr-2 h-4 w-4" />
          New Order
        </Button>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Order ID</TableHead>
              <TableHead className='hidden sm:table-cell'>Customer</TableHead>
              <TableHead className='hidden md:table-cell'>Date</TableHead>
              <TableHead>Status</TableHead>
              <TableHead className="text-right">Amount</TableHead>
              <TableHead className="text-center">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {orders.map((order) => (
              <TableRow key={order.id}>
                <TableCell className="font-medium">{order.id}</TableCell>
                <TableCell className='hidden sm:table-cell'>{order.customer}</TableCell>
                <TableCell className='hidden md:table-cell'>{order.date}</TableCell>
                <TableCell>
                  <Badge variant={getStatusVariant(order.status) as any}>{order.status}</Badge>
                </TableCell>
                <TableCell className="text-right">${order.amount.toFixed(2)}</TableCell>
                <TableCell className="text-center">
                  <Button variant="outline" size="sm">
                    <FileText className="mr-2 h-4 w-4" />
                    Print Label
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}
