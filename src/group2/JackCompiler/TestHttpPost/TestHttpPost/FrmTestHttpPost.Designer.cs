namespace TestHttpPost
{
	partial class FrmTestHttpPost
	{
		/// <summary>
		/// 必需的设计器变量。
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// 清理所有正在使用的资源。
		/// </summary>
		/// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows 窗体设计器生成的代码

		/// <summary>
		/// 设计器支持所需的方法 - 不要
		/// 使用代码编辑器修改此方法的内容。
		/// </summary>
		private void InitializeComponent()
		{
            this.pnlTool = new System.Windows.Forms.Panel();
            this.txtPostData = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.txtFilename = new System.Windows.Forms.TextBox();
            this.lblPostData = new System.Windows.Forms.Label();
            this.btnGo = new System.Windows.Forms.Button();
            this.txtUrl = new System.Windows.Forms.TextBox();
            this.cboMode = new System.Windows.Forms.ComboBox();
            this.cboResEncoding = new System.Windows.Forms.ComboBox();
            this.lblResEncoding = new System.Windows.Forms.Label();
            this.txtLog = new System.Windows.Forms.TextBox();
            this.pnlTool.SuspendLayout();
            this.SuspendLayout();
            // 
            // pnlTool
            // 
            this.pnlTool.Controls.Add(this.txtPostData);
            this.pnlTool.Controls.Add(this.label1);
            this.pnlTool.Controls.Add(this.txtFilename);
            this.pnlTool.Controls.Add(this.lblPostData);
            this.pnlTool.Controls.Add(this.btnGo);
            this.pnlTool.Controls.Add(this.txtUrl);
            this.pnlTool.Controls.Add(this.cboMode);
            this.pnlTool.Dock = System.Windows.Forms.DockStyle.Top;
            this.pnlTool.Location = new System.Drawing.Point(0, 0);
            this.pnlTool.Name = "pnlTool";
            this.pnlTool.Size = new System.Drawing.Size(702, 175);
            this.pnlTool.TabIndex = 0;
            // 
            // txtPostData
            // 
            this.txtPostData.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtPostData.Location = new System.Drawing.Point(86, 56);
            this.txtPostData.Multiline = true;
            this.txtPostData.Name = "txtPostData";
            this.txtPostData.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.txtPostData.Size = new System.Drawing.Size(616, 113);
            this.txtPostData.TabIndex = 3;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(3, 32);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(83, 12);
            this.label1.TabIndex = 8;
            this.label1.Text = "PostFilename:";
            // 
            // txtFilename
            // 
            this.txtFilename.Location = new System.Drawing.Point(86, 29);
            this.txtFilename.Name = "txtFilename";
            this.txtFilename.Size = new System.Drawing.Size(546, 21);
            this.txtFilename.TabIndex = 7;
            // 
            // lblPostData
            // 
            this.lblPostData.AutoSize = true;
            this.lblPostData.Location = new System.Drawing.Point(12, 69);
            this.lblPostData.Name = "lblPostData";
            this.lblPostData.Size = new System.Drawing.Size(65, 12);
            this.lblPostData.TabIndex = 4;
            this.lblPostData.Text = "Post Data:";
            // 
            // btnGo
            // 
            this.btnGo.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnGo.Location = new System.Drawing.Point(638, 0);
            this.btnGo.Name = "btnGo";
            this.btnGo.Size = new System.Drawing.Size(64, 23);
            this.btnGo.TabIndex = 2;
            this.btnGo.Text = "Go";
            this.btnGo.UseVisualStyleBackColor = true;
            this.btnGo.Click += new System.EventHandler(this.btnGo_Click);
            // 
            // txtUrl
            // 
            this.txtUrl.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtUrl.Location = new System.Drawing.Point(86, 2);
            this.txtUrl.Name = "txtUrl";
            this.txtUrl.Size = new System.Drawing.Size(546, 21);
            this.txtUrl.TabIndex = 1;
            this.txtUrl.Text = "http://localhost/";
            // 
            // cboMode
            // 
            this.cboMode.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboMode.FormattingEnabled = true;
            this.cboMode.Items.AddRange(new object[] {
            "GET",
            "POST"});
            this.cboMode.Location = new System.Drawing.Point(3, 2);
            this.cboMode.Name = "cboMode";
            this.cboMode.Size = new System.Drawing.Size(80, 20);
            this.cboMode.TabIndex = 0;
            // 
            // cboResEncoding
            // 
            this.cboResEncoding.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.cboResEncoding.DropDownHeight = 400;
            this.cboResEncoding.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboResEncoding.FormattingEnabled = true;
            this.cboResEncoding.IntegralHeight = false;
            this.cboResEncoding.Location = new System.Drawing.Point(122, 175);
            this.cboResEncoding.Name = "cboResEncoding";
            this.cboResEncoding.Size = new System.Drawing.Size(580, 20);
            this.cboResEncoding.TabIndex = 6;
            this.cboResEncoding.SelectedIndexChanged += new System.EventHandler(this.cboResEncoding_SelectedIndexChanged);
            // 
            // lblResEncoding
            // 
            this.lblResEncoding.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.lblResEncoding.AutoSize = true;
            this.lblResEncoding.Location = new System.Drawing.Point(3, 178);
            this.lblResEncoding.Name = "lblResEncoding";
            this.lblResEncoding.Size = new System.Drawing.Size(113, 12);
            this.lblResEncoding.TabIndex = 5;
            this.lblResEncoding.Text = "Response Encoding:";
            // 
            // txtLog
            // 
            this.txtLog.Location = new System.Drawing.Point(5, 201);
            this.txtLog.Multiline = true;
            this.txtLog.Name = "txtLog";
            this.txtLog.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.txtLog.Size = new System.Drawing.Size(697, 280);
            this.txtLog.TabIndex = 1;
            this.txtLog.WordWrap = false;
            // 
            // FrmTestHttpPost
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(702, 479);
            this.Controls.Add(this.txtLog);
            this.Controls.Add(this.cboResEncoding);
            this.Controls.Add(this.pnlTool);
            this.Controls.Add(this.lblResEncoding);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.Name = "FrmTestHttpPost";
            this.Text = "TestHttpPost";
            this.Load += new System.EventHandler(this.FrmTestHttpPost_Load);
            this.pnlTool.ResumeLayout(false);
            this.pnlTool.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Panel pnlTool;
		private System.Windows.Forms.ComboBox cboMode;
		private System.Windows.Forms.TextBox txtUrl;
		private System.Windows.Forms.Button btnGo;
		private System.Windows.Forms.TextBox txtPostData;
		private System.Windows.Forms.TextBox txtLog;
		private System.Windows.Forms.Label lblPostData;
		private System.Windows.Forms.Label lblResEncoding;
		private System.Windows.Forms.ComboBox cboResEncoding;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox txtFilename;
	}
}

