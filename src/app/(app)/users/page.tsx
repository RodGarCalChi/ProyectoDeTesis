import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Users } from "lucide-react";

export default function UsersPage() {
  return (
    <div className="flex justify-center items-center h-[calc(100vh-10rem)]">
        <Card className="w-full max-w-md text-center">
            <CardHeader>
                <div className="mx-auto bg-primary/10 p-4 rounded-full w-fit">
                    <Users className="h-12 w-12 text-primary" />
                </div>
                <CardTitle className="mt-4">User Management</CardTitle>
                <CardDescription>This feature is under construction.</CardDescription>
            </CardHeader>
            <CardContent>
                <p className="text-muted-foreground">
                    Role-based access control for managing user permissions and ensuring data security will be available here soon.
                </p>
            </CardContent>
        </Card>
    </div>
  );
}
