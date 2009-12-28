namespace Ciao_
{
    partial class ChatControl
    {
        /// <summary> 
        /// Variable nécessaire au concepteur.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Nettoyage des ressources utilisées.
        /// </summary>
        /// <param name="disposing">true si les ressources managées doivent être supprimées ; sinon, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Code généré par le Concepteur de composants

        /// <summary> 
        /// Méthode requise pour la prise en charge du concepteur - ne modifiez pas 
        /// le contenu de cette méthode avec l'éditeur de code.
        /// </summary>
        private void InitializeComponent()
        {
            this.chatText = new System.Windows.Forms.TextBox();
            this.textSend = new System.Windows.Forms.TextBox();
            this.buttonSend = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // chatText
            // 
            this.chatText.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.chatText.Location = new System.Drawing.Point(3, 3);
            this.chatText.Multiline = true;
            this.chatText.Name = "chatText";
            this.chatText.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.chatText.Size = new System.Drawing.Size(332, 152);
            this.chatText.TabIndex = 0;
            // 
            // textSend
            // 
            this.textSend.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.textSend.Location = new System.Drawing.Point(3, 161);
            this.textSend.Name = "textSend";
            this.textSend.Size = new System.Drawing.Size(279, 20);
            this.textSend.TabIndex = 1;
            this.textSend.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.textSend_KeyPress);
            // 
            // buttonSend
            // 
            this.buttonSend.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.buttonSend.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.buttonSend.Location = new System.Drawing.Point(288, 159);
            this.buttonSend.Name = "buttonSend";
            this.buttonSend.Size = new System.Drawing.Size(47, 23);
            this.buttonSend.TabIndex = 2;
            this.buttonSend.Text = "Send";
            this.buttonSend.UseVisualStyleBackColor = true;
            this.buttonSend.Click += new System.EventHandler(this.buttonSend_Click);
            // 
            // ChatControl
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.buttonSend);
            this.Controls.Add(this.textSend);
            this.Controls.Add(this.chatText);
            this.Name = "ChatControl";
            this.Size = new System.Drawing.Size(338, 184);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox chatText;
        private System.Windows.Forms.TextBox textSend;
        private System.Windows.Forms.Button buttonSend;
    }
}
