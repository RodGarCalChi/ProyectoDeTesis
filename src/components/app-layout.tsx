
import React from 'react';
import { SidebarProvider } from '@/components/ui/sidebar';
import AppHeader from '@/components/app-header';
import Nav from '@/components/nav';

export default function AppLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <SidebarProvider>
            <div className="flex">
                <Nav />
                <main className="flex-1 flex flex-col">
                    <AppHeader />
                    <div className="flex-1 overflow-y-auto p-4 lg:p-6">
                        {children}
                    </div>
                </main>
            </div>
        </SidebarProvider>
    );
}
